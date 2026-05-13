package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "VANCHUYEN", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_VC_MAVANDONTHUONG", columnNames = "MAVANDONTHUONG")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class VanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAVC")
    private Long maVc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAHD", foreignKey = @ForeignKey(name = "FK_VC_HD"))
    private HoaDon hoaDon;

    /** GiaoHangNhanh | GiaoHangTietKiem | ViettelPost */
    @Column(name = "DONVIVANCHUYEN", length = 50)
    private String donViVanChuyen;

    @Column(name = "MAVANDONTHUONG", length = 50, unique = true)
    private String maVanDonThuong;

    @Column(name = "PHIVANCHUYEN", precision = 15, scale = 2)
    private BigDecimal phiVanChuyen;

    /** ChoLayHang | DangVanChuyen | DaGiao | HoanHang */
    @Column(name = "TRANGTHAIVC", length = 20)
    private String trangThaiVc;

    @Column(name = "NGAYDUKIEN")
    private LocalDate ngayDuKien;

    @Column(name = "NGAYGIAO")
    private LocalDateTime ngayGiao;
}
