package com.hqlcsdt.hqlcsdl.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {
    private Long matk;
    private String manv;
    private Long manhom;
    private String tennhom;
    private NhanVienInfo nhanvien;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NhanVienInfo {
        private String hoten;
        private String chucvu;
        private Long mach;
        private String sdt;
        private String diachi;
        private LocalDate ngaysinh;
        private String gioitinh;
    }
}
