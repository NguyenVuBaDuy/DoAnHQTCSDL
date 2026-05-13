package com.hqlcsdt.hqlcsdl.repository;

import com.hqlcsdt.hqlcsdl.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long>, JpaSpecificationExecutor<TaiKhoan> {

    /**
     * Tìm tài khoản theo MANV — JOIN FETCH NHOM + NHANVIEN để tránh N+1.
     * Dùng cho login: query 1 lần lấy đủ thông tin.
     */
    @Query("SELECT tk FROM TaiKhoan tk " +
           "JOIN FETCH tk.nhom " +
           "JOIN FETCH tk.nhanVien nv " +
           "LEFT JOIN FETCH nv.cuaHang " +
           "WHERE nv.maNv = :manv")
    Optional<TaiKhoan> findByManvWithDetails(@Param("manv") String manv);

    /**
     * Tìm tài khoản theo MATK — JOIN FETCH để lấy đủ thông tin cho /auth/me và refresh.
     */
    @Query("SELECT tk FROM TaiKhoan tk " +
           "JOIN FETCH tk.nhom " +
           "JOIN FETCH tk.nhanVien nv " +
           "LEFT JOIN FETCH nv.cuaHang " +
           "WHERE tk.maTk = :matk")
    Optional<TaiKhoan> findByMaTkWithDetails(@Param("matk") Long matk);
}
