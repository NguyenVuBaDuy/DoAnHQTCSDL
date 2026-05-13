package com.hqlcsdt.hqlcsdl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienResponse {
    private String maNv;
    private Long maCh;
    private String tenCh;
    private String cccd;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String sdt;
    private String diaChi;
    private String chucVu;
    
    // Tai khoan info
    private Long maNhom;
    private String tenNhom;
    private String trangThai;
}
