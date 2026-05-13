package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.NhanVienRequest;
import com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.NhanVienRepository;
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

    private final NhanVienRepository nhanVienRepository;
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
            // Nếu cố tình filter cửa hàng khác -> lỗi hoặc ép về cửa hàng của mình
            if (machFilter != null && !machFilter.equals(mach)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        } else {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return nhanVienRepository.searchNhanVien(mach, chucvu, trangthai, search, pageable);
    }

    /**
     * Lấy thông tin chi tiết nhân viên
     */
    public NhanVienResponse getDetailByMaNv(JwtUserPrincipal principal, String maNv) {
        NhanVienResponse nv = nhanVienRepository.getDetailByMaNv(maNv)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if ("QuanLyCuaHang".equals(principal.getTennhom())) {
            if (nv.getMaCh() == null || !nv.getMaCh().equals(principal.getMach())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        } else if (!"Admin".equals(principal.getTennhom())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return nv;
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
}
