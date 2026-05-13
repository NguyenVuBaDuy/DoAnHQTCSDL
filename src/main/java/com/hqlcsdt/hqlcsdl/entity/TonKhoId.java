package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TonKhoId implements Serializable {

    @Column(name = "MACH")
    private Long maCh;

    @Column(name = "MABIENTHE")
    private Long maBienThe;
}
