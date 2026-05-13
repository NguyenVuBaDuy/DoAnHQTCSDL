package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VOUCHER")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    /** Mã nhập tay: VD SALE11, BIRTHDAY2024 */
    @Id
    @Column(name = "MAVOUCHER", length = 50)
    private String maVoucher;

    /** VD: Sale sinh nhật thương hiệu, Đôi đôi 2/2 */
    @Column(name = "TENVOUCHER", nullable = false, length = 200)
    private String tenVoucher;

    /** PhanTram | SoTienCoDinh */
    @Column(name = "LOAI", nullable = false, length = 20)
    private String loai;

    /** 10 = giảm 10% | 50000 = giảm 50k */
    @Column(name = "GIATRI", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaTri;

    /** Giảm tối đa — chỉ dùng khi LOAI = PhanTram */
    @Column(name = "GIATRI_TOI_DA", precision = 15, scale = 2)
    private BigDecimal giaTriToiDa;

    /** Giá trị đơn tối thiểu để áp dụng */
    @Column(name = "DIEUKIEN_TOITHIEU", precision = 15, scale = 2)
    private BigDecimal dieuKienToiThieu = BigDecimal.ZERO;

    /** Null = không giới hạn */
    @Column(name = "SOLUONG")
    private Integer soLuong;

    @Column(name = "SOLUONG_DA_DUNG")
    private Integer soLuongDaDung = 0;

    @Column(name = "NGAYBATDAU", nullable = false)
    private LocalDateTime ngayBatDau;

    @Column(name = "NGAYHETHAN", nullable = false)
    private LocalDateTime ngayHetHan;

    /** HoatDong | TamDung | HetHan */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;

    @Column(name = "GHICHU", length = 500)
    private String ghiChu;
}
