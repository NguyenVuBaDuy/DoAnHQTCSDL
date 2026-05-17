package com.hqlcsdt.hqlcsdl.repository;

import com.hqlcsdt.hqlcsdl.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    boolean existsBySdt(String sdt);
    boolean existsByEmail(String email);
}
