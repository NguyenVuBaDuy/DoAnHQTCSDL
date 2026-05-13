package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "KHACHHANG", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_KH_SDT", columnNames = "SDT")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAKH")
    private Long maKh;

    @Column(name = "HOTEN", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "SDT", length = 15, unique = true)
    private String sdt;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "NGAYSINH")
    private LocalDate ngaySinh;

    @Column(name = "GIOITINH", length = 10)
    private String gioiTinh;

    @Column(name = "DIACHI", length = 255)
    private String diaChi;

    @Column(name = "NGAYDANGKY")
    private LocalDate ngayDangKy;
}
