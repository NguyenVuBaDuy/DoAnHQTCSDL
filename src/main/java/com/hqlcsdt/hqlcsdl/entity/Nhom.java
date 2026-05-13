package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NHOM")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nhom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANHOM")
    private Long maNhom;

    @Column(name = "TENNHOM", nullable = false, length = 50)
    private String tenNhom;
}
