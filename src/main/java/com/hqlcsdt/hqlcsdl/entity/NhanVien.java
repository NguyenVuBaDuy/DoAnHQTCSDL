package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "NHANVIEN", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_NHANVIEN_CCCD", columnNames = "CCCD")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {

    /** PK: format YYYYxxxx, sinh tự động qua trigger + sequence SEQ_NV_SO */
    @Id
    @Column(name = "MANV", length = 20)
    private String maNv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH", foreignKey = @ForeignKey(name = "FK_NV_CH"))
    private CuaHang cuaHang;

    @Column(name = "CCCD", length = 20, unique = true)
    private String cccd;

    @Column(name = "HOTEN", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "NGAYSINH")
    private LocalDate ngaySinh;

    @Column(name = "GIOITINH", length = 10)
    private String gioiTinh;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DIACHI", length = 255)
    private String diaChi;

    @Column(name = "CHUCVU", length = 50)
    private String chucVu;
}
