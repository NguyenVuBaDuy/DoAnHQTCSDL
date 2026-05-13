package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DIACHIGIAOHANG")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiGiaoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MADCGH")
    private Long maDcgh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAKH", foreignKey = @ForeignKey(name = "FK_DCGH_KH"))
    private KhachHang khachHang;

    @Column(name = "HOTEN_NN", nullable = false, length = 100)
    private String hoTenNguoiNhan;

    @Column(name = "SDT_NN", nullable = false, length = 15)
    private String sdtNguoiNhan;

    @Column(name = "TINH", length = 50)
    private String tinh;

    @Column(name = "HUYEN", length = 50)
    private String huyen;

    @Column(name = "XA", length = 50)
    private String xa;

    @Column(name = "DIACHICT", length = 255)
    private String diaChiCt;

    /** NhaRieng | VanPhong | Khac */
    @Column(name = "LOAI", length = 20)
    private String loai;

    @Column(name = "MACDINH")
    private Integer macDinh = 0;
}
