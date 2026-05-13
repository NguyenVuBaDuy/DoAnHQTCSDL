package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CHITIETHOADON")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    @EmbeddedId
    private ChiTietHoaDonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHd")
    @JoinColumn(name = "MAHD", foreignKey = @ForeignKey(name = "FK_CTHD_HD"))
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maSn")
    @JoinColumn(name = "MASN", foreignKey = @ForeignKey(name = "FK_CTHD_SN"))
    private SerialNumber serialNumber;

    @Column(name = "DONGIA", nullable = false, precision = 15, scale = 2)
    private BigDecimal donGia;

    @Column(name = "GIAMGIA", precision = 15, scale = 2)
    private BigDecimal giamGia = BigDecimal.ZERO;
}
