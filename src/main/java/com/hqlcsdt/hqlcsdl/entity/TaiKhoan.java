package com.hqlcsdt.hqlcsdl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAIKHOAN", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_TAIKHOAN_MANV", columnNames = "MANV")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATK")
    private Long maTk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANHOM", foreignKey = @ForeignKey(name = "FK_TK_NHOM"))
    private Nhom nhom;

    /** Mã nhân viên dùng để đăng nhập */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANV", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "FK_TK_NV"))
    private NhanVien nhanVien;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    /** HoatDong | KhoaCung | KhoaTam */
    @Column(name = "TRANGTHAI", length = 20)
    private String trangThai;
}
