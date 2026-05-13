package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SERIALNUMBER", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_SN_SERIAL_IMEI", columnNames = "SERIAL_IMEI")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SerialNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MASN")
    private Long maSn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MABIENTHE", foreignKey = @ForeignKey(name = "FK_SN_BT"))
    private BienThe bienThe;

    /** Serial đang nằm ở cửa hàng nào. Null nếu đã bán */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACH", foreignKey = @ForeignKey(name = "FK_SN_CH"))
    private CuaHang cuaHang;

    @Column(name = "SERIAL_IMEI", nullable = false, length = 50, unique = true)
    private String serialImei;

    /** TonKho | DaBan | HongHoc */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
