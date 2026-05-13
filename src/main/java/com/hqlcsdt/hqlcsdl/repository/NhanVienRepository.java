package com.hqlcsdt.hqlcsdl.repository;

import com.hqlcsdt.hqlcsdl.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    @Query("SELECT new com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse(" +
           "nv.maNv, nv.cuaHang.maCh, nv.cuaHang.tenCh, nv.cccd, nv.hoTen, " +
           "nv.ngaySinh, nv.gioiTinh, nv.sdt, nv.diaChi, nv.chucVu, " +
           "tk.nhom.maNhom, tk.nhom.tenNhom, tk.trangThai) " +
           "FROM TaiKhoan tk " +
           "JOIN tk.nhanVien nv " +
           "LEFT JOIN nv.cuaHang ch " +
           "WHERE (:mach IS NULL OR nv.cuaHang.maCh = :mach) " +
           "AND (:chucvu IS NULL OR nv.chucVu = :chucvu) " +
           "AND (:trangthai IS NULL OR tk.trangThai = :trangthai) " +
           "AND (:search IS NULL OR LOWER(nv.maNv) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(nv.hoTen) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse> searchNhanVien(
            @Param("mach") Long mach,
            @Param("chucvu") String chucvu,
            @Param("trangthai") String trangthai,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT new com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse(" +
           "nv.maNv, nv.cuaHang.maCh, nv.cuaHang.tenCh, nv.cccd, nv.hoTen, " +
           "nv.ngaySinh, nv.gioiTinh, nv.sdt, nv.diaChi, nv.chucVu, " +
           "tk.nhom.maNhom, tk.nhom.tenNhom, tk.trangThai) " +
           "FROM TaiKhoan tk " +
           "JOIN tk.nhanVien nv " +
           "LEFT JOIN nv.cuaHang ch " +
           "WHERE nv.maNv = :maNv")
    Optional<com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse> getDetailByMaNv(@Param("maNv") String maNv);
}
