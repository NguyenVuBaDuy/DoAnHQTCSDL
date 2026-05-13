package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DANHMUCSANPHAM")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhMucSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MADM")
    private Long maDm;

    /** Self-reference: danh mục cha */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MADM_CHA", foreignKey = @ForeignKey(name = "FK_DM_CHA"))
    private DanhMucSanPham danhMucCha;

    @Column(name = "TENDM", nullable = false, length = 100)
    private String tenDm;

    @Lob
    @Column(name = "MOTA")
    private String moTa;
}
