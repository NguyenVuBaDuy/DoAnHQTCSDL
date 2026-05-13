package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "CUAHANG")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuaHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MACH")
    private Long maCh;

    @Column(name = "TENCH", nullable = false, length = 100)
    private String tenCh;

    @Column(name = "DIACHI", length = 255)
    private String diaChi;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "NGAYKHAITRUONG")
    private LocalDate ngayKhaiTruong;

    /** HoatDong | DongCua | TamNgung */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
