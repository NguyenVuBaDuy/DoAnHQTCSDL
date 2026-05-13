package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHITIETPHIEUCHUYENKHO")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuChuyenKho {

    @EmbeddedId
    private ChiTietPhieuChuyenKhoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPck")
    @JoinColumn(name = "MAPCK", foreignKey = @ForeignKey(name = "FK_CTPCK_PCK"))
    private PhieuChuyenKho phieuChuyenKho;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maBienThe")
    @JoinColumn(name = "MABIENTHE", foreignKey = @ForeignKey(name = "FK_CTPCK_BT"))
    private BienThe bienThe;

    @Column(name = "SOLUONG", nullable = false)
    private Integer soLuong;
}
