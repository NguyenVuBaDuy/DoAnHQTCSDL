package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOADON")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAHD")
    private Long maHd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH", foreignKey = @ForeignKey(name = "FK_HD_CH"))
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANV", foreignKey = @ForeignKey(name = "FK_HD_NV"))
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAKH", foreignKey = @ForeignKey(name = "FK_HD_KH"))
    private KhachHang khachHang;

    /** Null nếu bán tại quầy */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MADCGH", foreignKey = @ForeignKey(name = "FK_HD_DCGH"))
    private DiaChiGiaoHang diaChiGiaoHang;

    /** Null nếu không dùng voucher */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAVOUCHER", foreignKey = @ForeignKey(name = "FK_HD_VOUCHER"))
    private Voucher voucher;

    @Column(name = "NGAYLAP")
    private LocalDateTime ngayLap;

    /** Snapshot tại thời điểm chốt đơn */
    @Column(name = "TONGTIEN", precision = 15, scale = 2)
    private BigDecimal tongTien;

    /** Số tiền thực tế đã giảm từ voucher */
    @Column(name = "GIATRI_GIAM", precision = 15, scale = 2)
    private BigDecimal giaTriGiam = BigDecimal.ZERO;

    @Column(name = "PHIVANCHUYEN", precision = 15, scale = 2)
    private BigDecimal phiVanChuyen = BigDecimal.ZERO;

    /** TienMat | ChuyenKhoan | QRCode | TraGop */
    @Column(name = "PTTT", length = 20)
    private String phuongThucThanhToan;

    /** TaiQuay | Online */
    @Column(name = "LOAIHD", length = 20)
    private String loaiHd;

    /** ChoDuyet | DaXacNhan | DangGiao | HoanThanh | DaHuy */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
