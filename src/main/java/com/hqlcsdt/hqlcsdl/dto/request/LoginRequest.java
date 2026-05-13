package com.hqlcsdt.hqlcsdl.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String manv;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
