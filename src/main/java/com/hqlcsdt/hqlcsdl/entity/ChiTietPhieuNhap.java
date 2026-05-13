package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CHITIETPHIEUNHAP")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhap {

    @EmbeddedId
    private ChiTietPhieuNhapId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPn")
    @JoinColumn(name = "MAPN", foreignKey = @ForeignKey(name = "FK_CTPN_PN"))
    private PhieuNhap phieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maBienThe")
    @JoinColumn(name = "MABIENTHE", foreignKey = @ForeignKey(name = "FK_CTPN_BT"))
    private BienThe bienThe;

    @Column(name = "SOLUONG", nullable = false)
    private Integer soLuong;

    @Column(name = "DONGIA", nullable = false, precision = 15, scale = 2)
    private BigDecimal donGia;
}
