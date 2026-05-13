package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChiTietPhieuNhapId implements Serializable {

    @Column(name = "MAPN")
    private Long maPn;

    @Column(name = "MABIENTHE")
    private Long maBienThe;
}
