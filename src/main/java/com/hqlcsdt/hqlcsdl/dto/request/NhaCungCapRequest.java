package com.hqlcsdt.hqlcsdl.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCapRequest {

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String tenNcc;

    private String diaChi;

    private String sdt;

    private String email;

    private String maSoThue;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai; // HoatDong | DungHopTac
}
