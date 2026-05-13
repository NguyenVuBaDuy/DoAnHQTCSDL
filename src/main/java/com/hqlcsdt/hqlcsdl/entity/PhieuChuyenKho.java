package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PHIEUCHUYENKHO")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuChuyenKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAPCK")
    private Long maPck;

    /** Cửa hàng xuất hàng */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH_NGUON", foreignKey = @ForeignKey(name = "FK_PCK_CH_NGUON"))
    private CuaHang cuaHangNguon;

    /** Cửa hàng nhận hàng */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH_DICH", foreignKey = @ForeignKey(name = "FK_PCK_CH_DICH"))
    private CuaHang cuaHangDich;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANV", foreignKey = @ForeignKey(name = "FK_PCK_NV"))
    private NhanVien nhanVien;

    @Column(name = "NGAYCHUYENKHO")
    private LocalDateTime ngayChuyenKho;

    @Column(name = "GHICHU", length = 500)
    private String ghiChu;

    /** ChoDuyet | DaChuyenKho | HuyPhieu */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
