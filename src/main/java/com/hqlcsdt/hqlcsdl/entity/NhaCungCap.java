package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NHACUNGCAP", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_NCC_MASOTHUE", columnNames = "MASOTHUE")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANCC")
    private Long maNcc;

    @Column(name = "TENNCC", nullable = false, length = 100)
    private String tenNcc;

    @Column(name = "DIACHI", length = 255)
    private String diaChi;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "MASOTHUE", length = 20, unique = true)
    private String maSoThue;

    /** HoatDong | DungHopTac */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
