package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.CuaHangRequest;
import com.hqlcsdt.hqlcsdl.entity.CuaHang;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.CuaHangRepository;
import com.hqlcsdt.hqlcsdl.utils.StoredProcedureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuaHangService {

    private final CuaHangRepository cuaHangRepository;
    private final StoredProcedureUtils sp;

    /**
     * Lấy danh sách tất cả cửa hàng.
     */
    public List<CuaHang> getAllCuaHang() {
        return cuaHangRepository.findAll();
    }

    /**
     * Lấy thông tin cửa hàng theo mã.
     */
    public CuaHang getCuaHangById(Long mach) {
        return cuaHangRepository.findById(mach)
                .orElseThrow(() -> new AppException(ErrorCode.STORE_NOT_FOUND));
    }

    /**
     * Thêm cửa hàng mới — gọi procedure INSERT_CUAHANG.
     * Procedure tự validate: trùng SĐT, trùng email, trạng thái hợp lệ.
     */
    @Transactional
    public void createCuaHang(CuaHangRequest request) {
        sp.call("INSERT_CUAHANG")
                .input("p_TENCH", request.getTenCh(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_EMAIL", request.getEmail(), String.class)
                .input("p_NGAYKHAITRUONG", request.getNgayKhaiTruong(), LocalDate.class)
                .input("p_TRANGTHAI", request.getTrangThai(), String.class)
                .execute();
    }

    /**
     * Cập nhật thông tin cửa hàng — gọi procedure UPDATE_CUAHANG.
     * Procedure tự validate: tồn tại, trùng SĐT/email với cửa hàng khác, trạng thái hợp lệ.
     */
    @Transactional
    public void updateCuaHang(Long mach, CuaHangRequest request) {
        sp.call("UPDATE_CUAHANG")
                .input("p_MACH", mach, Long.class)
                .input("p_TENCH", request.getTenCh(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_EMAIL", request.getEmail(), String.class)
                .input("p_NGAYKHAITRUONG", request.getNgayKhaiTruong(), LocalDate.class)
                .input("p_TRANGTHAI", request.getTrangThai(), String.class)
                .execute();
    }

    /**
     * Xóa cửa hàng — gọi procedure DELETE_CUAHANG.
     * Procedure tự validate: tồn tại, còn nhân viên, còn tồn kho.
     */
    @Transactional
    public void deleteCuaHang(Long mach) {
        sp.call("DELETE_CUAHANG")
                .input("p_MACH", mach, Long.class)
                .execute();
    }

    /**
     * Thay đổi trạng thái cửa hàng — gọi procedure CHANGE_STATUS_CUAHANG.
     * Procedure tự validate: tồn tại, trạng thái hợp lệ.
     */
    @Transactional
    public void changeStatus(Long mach, String trangThai) {
        sp.call("CHANGE_STATUS_CUAHANG")
                .input("p_MACH", mach, Long.class)
                .input("p_TRANGTHAI", trangThai, String.class)
                .execute();
    }
}
