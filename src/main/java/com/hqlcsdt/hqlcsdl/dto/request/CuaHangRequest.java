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
public class CuaHangRequest {

    @NotBlank(message = "Tên cửa hàng không được để trống")
    private String tenCh;

    private String diaChi;

    private String sdt;

    private String email;

    private LocalDate ngayKhaiTruong;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai; // HoatDong | DongCua | TamNgung
}
