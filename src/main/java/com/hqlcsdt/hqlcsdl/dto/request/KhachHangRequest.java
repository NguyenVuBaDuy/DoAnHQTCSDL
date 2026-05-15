package com.hqlcsdt.hqlcsdl.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangRequest {

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String hoTen;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String sdt;

    private String email;

    private LocalDate ngaySinh;

    private String gioiTinh;

    private String diaChi;
}
