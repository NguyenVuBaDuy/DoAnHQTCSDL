package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChiTietPhieuChuyenKhoId implements Serializable {

    @Column(name = "MAPCK")
    private Long maPck;

    @Column(name = "MABIENTHE")
    private Long maBienThe;
}
