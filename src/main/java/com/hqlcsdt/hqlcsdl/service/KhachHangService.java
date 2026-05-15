package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.KhachHangRequest;
import com.hqlcsdt.hqlcsdl.entity.KhachHang;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    public KhachHang getKhachHangById(Long id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Transactional
    public void createKhachHang(KhachHangRequest request) {
        if (khachHangRepository.existsBySdt(request.getSdt())) {
            throw new AppException(ErrorCode.CUSTOMER_PHONE_EXISTS);
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty() && khachHangRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.CUSTOMER_EMAIL_EXISTS);
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(request.getHoTen());
        khachHang.setSdt(request.getSdt());
        khachHang.setEmail(request.getEmail());
        khachHang.setNgaySinh(request.getNgaySinh());
        khachHang.setGioiTinh(request.getGioiTinh());
        khachHang.setDiaChi(request.getDiaChi());
        khachHang.setNgayDangKy(LocalDate.now());

        khachHangRepository.save(khachHang);
    }

    @Transactional
    public void updateKhachHang(Long id, KhachHangRequest request) {
        KhachHang khachHang = getKhachHangById(id);

        if (!khachHang.getSdt().equals(request.getSdt()) && khachHangRepository.existsBySdt(request.getSdt())) {
            throw new AppException(ErrorCode.CUSTOMER_PHONE_EXISTS);
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty() 
            && (khachHang.getEmail() == null || !khachHang.getEmail().equals(request.getEmail())) 
            && khachHangRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.CUSTOMER_EMAIL_EXISTS);
        }

        khachHang.setHoTen(request.getHoTen());
        khachHang.setSdt(request.getSdt());
        khachHang.setEmail(request.getEmail());
        khachHang.setNgaySinh(request.getNgaySinh());
        khachHang.setGioiTinh(request.getGioiTinh());
        khachHang.setDiaChi(request.getDiaChi());

        khachHangRepository.save(khachHang);
    }

    @Transactional
    public void deleteKhachHang(Long id) {
        if (!khachHangRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        khachHangRepository.deleteById(id);
    }
}
