package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.NhanVienRequest;
import com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse;
import com.hqlcsdt.hqlcsdl.entity.TaiKhoan;
import com.hqlcsdt.hqlcsdl.entity.Nhom;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.TaiKhoanRepository;
import com.hqlcsdt.hqlcsdl.repository.NhomRepository;
import com.hqlcsdt.hqlcsdl.security.JwtUserPrincipal;
import com.hqlcsdt.hqlcsdl.utils.StoredProcedureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NhanVienService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final NhomRepository nhomRepository;
    private final StoredProcedureUtils sp;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lấy danh sách nhân viên có phân trang và filter.
     * Admin: thấy tất cả
     * QuanLyCuaHang: chỉ thấy nhân viên của cửa hàng mình (mach lấy từ JwtUserPrincipal).
     */
    public Page<NhanVienResponse> searchNhanVien(JwtUserPrincipal principal, Long machFilter, String chucvu, String trangthai, String search, Pageable pageable) {
        Long mach = null;
        
        // Phân quyền
        if ("Admin".equals(principal.getTennhom())) {
            mach = machFilter; // Admin có thể filter theo cửa hàng hoặc xem tất cả
        } else if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            mach = principal.getMach(); // Quản lý cửa hàng chỉ xem được cửa hàng của mình
            if (machFilter != null && !machFilter.equals(mach)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        } else {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        final Long finalMach = mach;
        org.springframework.data.jpa.domain.Specification<TaiKhoan> spec = (root, query, cb) -> {
            // Eager fetch để tránh N+1 nếu không phải countQuery
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("nhom", jakarta.persistence.criteria.JoinType.LEFT);
                jakarta.persistence.criteria.Fetch<Object, Object> nvFetch = root.fetch("nhanVien", jakarta.persistence.criteria.JoinType.INNER);
                nvFetch.fetch("cuaHang", jakarta.persistence.criteria.JoinType.LEFT);
            }

            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            jakarta.persistence.criteria.Join<Object, Object> nv = root.join("nhanVien");

            if (finalMach != null) {
                jakarta.persistence.criteria.Join<Object, Object> ch = nv.join("cuaHang", jakarta.persistence.criteria.JoinType.LEFT);
                predicates.add(cb.equal(ch.get("maCh"), finalMach));
            }
            if (chucvu != null && !chucvu.isBlank()) {
                predicates.add(cb.equal(nv.get("chucVu"), chucvu));
            }
            if (trangthai != null && !trangthai.isBlank()) {
                predicates.add(cb.equal(root.get("trangThai"), trangthai));
            }
            if (search != null && !search.isBlank()) {
                String likePattern = "%" + search.toLowerCase() + "%";
                jakarta.persistence.criteria.Predicate matchMaNv = cb.like(cb.lower(nv.get("maNv")), likePattern);
                jakarta.persistence.criteria.Predicate matchHoTen = cb.like(cb.lower(nv.get("hoTen")), likePattern);
                predicates.add(cb.or(matchMaNv, matchHoTen));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Page<TaiKhoan> tkPage = taiKhoanRepository.findAll(spec, pageable);
        return tkPage.map(this::mapToDto);
    }

    /**
     * Lấy thông tin chi tiết nhân viên
     */
    public NhanVienResponse getDetailByMaNv(JwtUserPrincipal principal, String maNv) {
        TaiKhoan tk = taiKhoanRepository.findByManvWithDetails(maNv)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Long maCh = tk.getNhanVien().getCuaHang() != null ? tk.getNhanVien().getCuaHang().getMaCh() : null;

        if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            if (maCh == null || !maCh.equals(principal.getMach())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        } else if (!"Admin".equals(principal.getTennhom())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return mapToDto(tk);
    }

    private NhanVienResponse mapToDto(TaiKhoan tk) {
        return NhanVienResponse.builder()
                .maNv(tk.getNhanVien().getMaNv())
                .maCh(tk.getNhanVien().getCuaHang() != null ? tk.getNhanVien().getCuaHang().getMaCh() : null)
                .tenCh(tk.getNhanVien().getCuaHang() != null ? tk.getNhanVien().getCuaHang().getTenCh() : null)
                .cccd(tk.getNhanVien().getCccd())
                .hoTen(tk.getNhanVien().getHoTen())
                .ngaySinh(tk.getNhanVien().getNgaySinh())
                .gioiTinh(tk.getNhanVien().getGioiTinh())
                .sdt(tk.getNhanVien().getSdt())
                .diaChi(tk.getNhanVien().getDiaChi())
                .chucVu(tk.getNhanVien().getChucVu())
                .maNhom(tk.getNhom().getMaNhom())
                .tenNhom(tk.getNhom().getTenNhom())
                .trangThai(tk.getTrangThai())
                .build();
    }

    /**
     * Thêm nhân viên mới — gọi procedure INSERT_NHANVIEN.
     */
    @Transactional
    public void createNhanVien(JwtUserPrincipal principal, NhanVienRequest request) {
        // Kiểm tra quyền
        Long mach = request.getMaCh();
        if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            mach = principal.getMach(); // Ép về cửa hàng của quản lý
        } else if (!"Admin".equals(principal.getTennhom())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        sp.call("INSERT_NHANVIEN")
                .input("p_MACH", mach, Long.class)
                .input("p_CCCD", request.getCccd(), String.class)
                .input("p_HOTEN", request.getHoTen(), String.class)
                .input("p_NGAYSINH", request.getNgaySinh(), LocalDate.class)
                .input("p_GIOITINH", request.getGioiTinh(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_CHUCVU", request.getChucVu(), String.class)
                .input("p_MANHOM", request.getMaNhom(), Long.class)
                .input("p_PASSWORD", hashedPassword, String.class)
                .input("p_TRANGTHAI", request.getTrangThai(), String.class)
                .execute();
    }

    /**
     * Cập nhật thông tin nhân viên — gọi procedure UPDATE_NHANVIEN.
     */
    @Transactional
    public void updateNhanVien(JwtUserPrincipal principal, String maNv, NhanVienRequest request) {
        // Kiểm tra quyền trên nv này
        NhanVienResponse nv = getDetailByMaNv(principal, maNv);

        Long mach = request.getMaCh();
        if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            mach = principal.getMach(); // Ép về cửa hàng của quản lý
        }

        sp.call("UPDATE_NHANVIEN")
                .input("p_MANV", maNv, String.class)
                .input("p_MACH", mach, Long.class)
                .input("p_CCCD", request.getCccd(), String.class)
                .input("p_HOTEN", request.getHoTen(), String.class)
                .input("p_NGAYSINH", request.getNgaySinh(), LocalDate.class)
                .input("p_GIOITINH", request.getGioiTinh(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_CHUCVU", request.getChucVu(), String.class)
                .execute();
    }

    /**
     * Vô hiệu hóa nhân viên — gọi procedure DISABLE_NHANVIEN.
     */
    @Transactional
    public void disableNhanVien(JwtUserPrincipal principal, String maNv) {
        // Kiểm tra quyền trên nv này
        getDetailByMaNv(principal, maNv);

        sp.call("DISABLE_NHANVIEN")
                .input("p_MANV", maNv, String.class)
                .execute();
    }

    /**
     * Cập nhật nhóm quyền (Role) cho tài khoản
     */
    @Transactional
    public void updateRole(JwtUserPrincipal principal, String maNv, Long maNhom) {
        // Chỉ Admin mới được đổi quyền
        if (!"Admin".equals(principal.getTennhom())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        TaiKhoan tk = taiKhoanRepository.findByManvWithDetails(maNv)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Nhom nhom = nhomRepository.findById(maNhom)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_INPUT)); // Lỗi "Không tìm thấy nhóm"

        tk.setNhom(nhom);
        taiKhoanRepository.save(tk);
    }

    /**
     * Mở/khóa tài khoản trực tiếp (đổi trạng thái)
     */
    @Transactional
    public void updateStatus(JwtUserPrincipal principal, String maNv, String status) {
        if (status == null || (!status.equals("HoatDong") && !status.equals("KhoaCung") && !status.equals("KhoaTam"))) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        TaiKhoan tk = taiKhoanRepository.findByManvWithDetails(maNv)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        Long maCh = tk.getNhanVien().getCuaHang() != null ? tk.getNhanVien().getCuaHang().getMaCh() : null;

        // Phân quyền
        if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            if (maCh == null || !maCh.equals(principal.getMach())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        } else if (!"Admin".equals(principal.getTennhom())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        tk.setTrangThai(status);
        taiKhoanRepository.save(tk);
    }
}
