package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.CuaHangRequest;
import com.hqlcsdt.hqlcsdl.entity.CuaHang;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.CuaHangRepository;
import com.hqlcsdt.hqlcsdl.utils.StoredProcedureUtils;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuaHangService {

    private final CuaHangRepository cuaHangRepository;
    private final StoredProcedureUtils storedProcedureUtils;

    public List<CuaHang> getAllCuaHang() {
        return cuaHangRepository.findAll();
    }

    public CuaHang getCuaHangById(Long mach) {
        return cuaHangRepository.findById(mach)
                .orElseThrow(() -> new AppException(ErrorCode.STORE_NOT_FOUND));
    }

    @Transactional
    public CuaHang createCuaHang(CuaHangRequest request) {
        StoredProcedureQuery query = storedProcedureUtils.createQuery("INSERT_CUAHANG");
        
        query.registerStoredProcedureParameter("p_tench", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_diachi", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_sdt", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_ngaykhaitruong", java.time.LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_trangthai", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mach", Long.class, ParameterMode.OUT);

        query.setParameter("p_tench", request.getTenCh());
        query.setParameter("p_diachi", request.getDiaChi());
        query.setParameter("p_sdt", request.getSdt());
        query.setParameter("p_email", request.getEmail());
        query.setParameter("p_ngaykhaitruong", request.getNgayKhaiTruong());
        query.setParameter("p_trangthai", request.getTrangThai());

        query.execute();

        Number machNumber = (Number) query.getOutputParameterValue("p_mach");
        Long mach = machNumber.longValue();

        return getCuaHangById(mach);
    }

    @Transactional
    public CuaHang updateCuaHang(Long mach, CuaHangRequest request) {
        // Validate existence
        getCuaHangById(mach);

        StoredProcedureQuery query = storedProcedureUtils.createQuery("UPDATE_CUAHANG");

        query.registerStoredProcedureParameter("p_mach", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_tench", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_diachi", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_sdt", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_ngaykhaitruong", java.time.LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_trangthai", String.class, ParameterMode.IN);

        query.setParameter("p_mach", mach);
        query.setParameter("p_tench", request.getTenCh());
        query.setParameter("p_diachi", request.getDiaChi());
        query.setParameter("p_sdt", request.getSdt());
        query.setParameter("p_email", request.getEmail());
        query.setParameter("p_ngaykhaitruong", request.getNgayKhaiTruong());
        query.setParameter("p_trangthai", request.getTrangThai());

        query.execute();

        return getCuaHangById(mach);
    }

    @Transactional
    public void deleteCuaHang(Long mach) {
        // Validate existence
        getCuaHangById(mach);

        StoredProcedureQuery query = storedProcedureUtils.createQuery("DELETE_CUAHANG");
        query.registerStoredProcedureParameter("p_mach", Long.class, ParameterMode.IN);
        query.setParameter("p_mach", mach);

        query.execute();
    }

    @Transactional
    public void changeStatus(Long mach, String status) {
        // Validate existence
        getCuaHangById(mach);

        StoredProcedureQuery query = storedProcedureUtils.createQuery("CHANGE_STATUS_CUAHANG");
        query.registerStoredProcedureParameter("p_mach", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_trangthai", String.class, ParameterMode.IN);
        
        query.setParameter("p_mach", mach);
        query.setParameter("p_trangthai", status);

        query.execute();
    }
}
