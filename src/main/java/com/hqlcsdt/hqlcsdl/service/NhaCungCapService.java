package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.NhaCungCapRequest;
import com.hqlcsdt.hqlcsdl.entity.NhaCungCap;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.NhaCungCapRepository;
import com.hqlcsdt.hqlcsdl.utils.StoredProcedureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;
    private final StoredProcedureUtils sp;

    /**
     * Lấy danh sách tất cả nhà cung cấp.
     */
    public List<NhaCungCap> getAllNhaCungCap() {
        return nhaCungCapRepository.findAll();
    }

    /**
     * Lấy thông tin nhà cung cấp theo mã.
     */
    public NhaCungCap getNhaCungCapById(Long maNcc) {
        return nhaCungCapRepository.findById(maNcc)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
    }

    /**
     * Thêm nhà cung cấp mới — gọi procedure INSERT_NHACUNGCAP.
     * Procedure tự validate: trùng mã số thuế, trùng SĐT, trùng email, trạng thái
     * hợp lệ.
     */
    @Transactional
    public void createNhaCungCap(NhaCungCapRequest request) {
        sp.call("INSERT_NHACUNGCAP")
                .input("p_TENNCC", request.getTenNcc(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_EMAIL", request.getEmail(), String.class)
                .input("p_MASOTHUE", request.getMaSoThue(), String.class)
                .input("p_TRANGTHAI", request.getTrangThai(), String.class)
                .execute();
    }

    /**
     * Cập nhật thông tin nhà cung cấp — gọi procedure UPDATE_NHACUNGCAP.
     * Procedure tự validate: tồn tại, trùng mã số thuế/SĐT/email với nhà cung cấp
     * khác, trạng thái hợp lệ.
     */
    @Transactional
    public void updateNhaCungCap(Long maNcc, NhaCungCapRequest request) {
        sp.call("UPDATE_NHACUNGCAP")
                .input("p_MANCC", maNcc, Long.class)
                .input("p_TENNCC", request.getTenNcc(), String.class)
                .input("p_DIACHI", request.getDiaChi(), String.class)
                .input("p_SDT", request.getSdt(), String.class)
                .input("p_EMAIL", request.getEmail(), String.class)
                .input("p_MASOTHUE", request.getMaSoThue(), String.class)
                .input("p_TRANGTHAI", request.getTrangThai(), String.class)
                .execute();
    }

    /**
     * Xóa nhà cung cấp — gọi procedure DELETE_NHACUNGCAP.
     * Procedure tự validate: tồn tại, còn đơn hàng, còn hợp đồng.
     */
    @Transactional
    public void deleteNhaCungCap(Long maNcc) {
        sp.call("DELETE_NHACUNGCAP")
                .input("p_MANCC", maNcc, Long.class)
                .execute();
    }

    /**
     * Thay đổi trạng thái nhà cung cấp — gọi procedure CHANGE_STATUS_NHACUNGCAP.
     * Procedure tự validate: tồn tại, trạng thái hợp lệ.
     */
    @Transactional
    public void changeStatus(Long maNcc, String trangThai) {
        sp.call("CHANGE_STATUS_NHACUNGCAP")
                .input("p_MANCC", maNcc, Long.class)
                .input("p_TRANGTHAI", trangThai, String.class)
                .execute();
    }
}
