package com.hqlcsdt.hqlcsdl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienRequest {

    @NotNull(message = "Mã nhóm quyền không được trống")
    private Long maNhom;

    private Long maCh; // Có thể null nếu là Admin tổng

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    @NotBlank(message = "CCCD không được để trống")
    private String cccd;

    private LocalDate ngaySinh;

    private String gioiTinh;

    private String sdt;

    private String diaChi;

    private String chucVu;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password; // Dùng khi tạo mới

    private String trangThai; // Dùng khi tạo mới hoặc cập nhật
}
