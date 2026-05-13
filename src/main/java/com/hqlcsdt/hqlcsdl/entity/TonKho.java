package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TONKHO")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TonKho {

    @EmbeddedId
    private TonKhoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maCh")
    @JoinColumn(name = "MACH", foreignKey = @ForeignKey(name = "FK_TK_CH"))
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maBienThe")
    @JoinColumn(name = "MABIENTHE", foreignKey = @ForeignKey(name = "FK_TK_BT"))
    private BienThe bienThe;

    @Column(name = "SOLUONG", nullable = false)
    private Integer soLuong = 0;
}
