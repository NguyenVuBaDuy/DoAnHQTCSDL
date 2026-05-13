package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PHIEUNHAP")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAPN")
    private Long maPn;

    /** Nhập vào kho của cửa hàng nào */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH", foreignKey = @ForeignKey(name = "FK_PN_CH"))
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANCC", foreignKey = @ForeignKey(name = "FK_PN_NCC"))
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANV", foreignKey = @ForeignKey(name = "FK_PN_NV"))
    private NhanVien nhanVien;

    @Column(name = "NGAYNHAP")
    private LocalDateTime ngayNhap;

    @Column(name = "TONGTIEN", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "GHICHU", length = 500)
    private String ghiChu;

    /** NhapKho | HuyPhieu */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
