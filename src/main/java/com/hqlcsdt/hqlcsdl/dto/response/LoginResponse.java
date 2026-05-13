package com.hqlcsdt.hqlcsdl.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfo user;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long matk;
        private String manv;
        private String hoten;
        private Long manhom;
        private String tennhom;
        private String chucvu;
        private Long mach;
    }
}
