package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SANPHAM")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MASP")
    private Long maSp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MADM", foreignKey = @ForeignKey(name = "FK_SP_DM"))
    private DanhMucSanPham danhMuc;

    @Column(name = "TENSP", nullable = false, length = 200)
    private String tenSp;

    @Column(name = "THUONGHIEU", length = 100)
    private String thuongHieu;

    @Lob
    @Column(name = "MOTA")
    private String moTa;

    @Column(name = "ANH", length = 500)
    private String anh;

    /** DangBan | NgungBan | HetHang */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
