package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "BIENTHE", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_BIENTHE_SKU", columnNames = "SKU"),
    @UniqueConstraint(name = "UQ_BIENTHE_BARCODE", columnNames = "BARCODE")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BienThe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MABIENTHE")
    private Long maBienThe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASP", foreignKey = @ForeignKey(name = "FK_BT_SP"))
    private SanPham sanPham;

    @Column(name = "SKU", length = 50, unique = true)
    private String sku;

    @Column(name = "BARCODE", length = 50, unique = true)
    private String barcode;

    @Column(name = "MAUSAC", length = 50)
    private String mauSac;

    @Column(name = "DUNGLUONG", length = 50)
    private String dungLuong;

    @Column(name = "KICHTHUOC", length = 50)
    private String kichThuoc;

    @Column(name = "GIA_NHAP", precision = 15, scale = 2)
    private BigDecimal giaNhap;

    @Column(name = "GIA_BAN", precision = 15, scale = 2)
    private BigDecimal giaBan;

    /** DangBan | NgungBan */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
