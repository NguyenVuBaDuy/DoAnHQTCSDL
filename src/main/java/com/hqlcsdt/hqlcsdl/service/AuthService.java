package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.request.LoginRequest;
import com.hqlcsdt.hqlcsdl.dto.request.ResetPasswordRequest;
import com.hqlcsdt.hqlcsdl.dto.response.LoginResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MeResponse;
import com.hqlcsdt.hqlcsdl.entity.CuaHang;
import com.hqlcsdt.hqlcsdl.entity.NhanVien;
import com.hqlcsdt.hqlcsdl.entity.Nhom;
import com.hqlcsdt.hqlcsdl.entity.TaiKhoan;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import com.hqlcsdt.hqlcsdl.repository.TaiKhoanRepository;
import com.hqlcsdt.hqlcsdl.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * POST /auth/login
     * Query JOIN: TAIKHOAN → NHOM → NHANVIEN theo MANV
     */
    public LoginResponse login(LoginRequest request) {
        // 1. Tìm tài khoản theo MANV (JOIN FETCH nhom + nhanVien + cuaHang)
        TaiKhoan taiKhoan = taiKhoanRepository.findByManvWithDetails(request.getManv())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        // 2. Kiểm tra password
        if (!passwordEncoder.matches(request.getPassword(), taiKhoan.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        // 3. Kiểm tra trạng thái tài khoản
        checkAccountStatus(taiKhoan);

        // 4. Lấy thông tin liên quan
        NhanVien nv = taiKhoan.getNhanVien();
        Nhom nhom = taiKhoan.getNhom();
        CuaHang ch = nv.getCuaHang();

        Long mach = (ch != null) ? ch.getMaCh() : null;

        // 5. Ký access token
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("matk", taiKhoan.getMaTk());
        accessClaims.put("manv", nv.getMaNv());
        accessClaims.put("manhom", nhom.getMaNhom());
        accessClaims.put("tennhom", nhom.getTenNhom());
        accessClaims.put("mach", mach);

        String accessToken = jwtTokenProvider.generateAccessToken(accessClaims);

        // 6. Ký refresh token
        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("matk", taiKhoan.getMaTk());
        refreshClaims.put("manv", nv.getMaNv());

        String refreshToken = jwtTokenProvider.generateRefreshToken(refreshClaims);

        // 7. Trả về response
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .matk(taiKhoan.getMaTk())
                .manv(nv.getMaNv())
                .hoten(nv.getHoTen())
                .manhom(nhom.getMaNhom())
                .tennhom(nhom.getTenNhom())
                .chucvu(nv.getChucVu())
                .mach(mach)
                .build();

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();
    }

    /**
     * POST /auth/refresh
     * Verify refreshToken → query lại để lấy role mới nhất → ký accessToken mới.
     */
    public String refresh(String refreshToken) {
        // 1. Verify refresh token
        Claims claims = jwtTokenProvider.parseRefreshToken(refreshToken);

        Long matk = claims.get("matk", Long.class);

        // 2. Query lại để lấy thông tin mới nhất (phòng trường hợp role bị đổi)
        TaiKhoan taiKhoan = taiKhoanRepository.findByMaTkWithDetails(matk)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 3. Kiểm tra trạng thái
        checkAccountStatus(taiKhoan);

        NhanVien nv = taiKhoan.getNhanVien();
        Nhom nhom = taiKhoan.getNhom();
        CuaHang ch = nv.getCuaHang();
        Long mach = (ch != null) ? ch.getMaCh() : null;

        // 4. Ký access token mới
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("matk", taiKhoan.getMaTk());
        accessClaims.put("manv", nv.getMaNv());
        accessClaims.put("manhom", nhom.getMaNhom());
        accessClaims.put("tennhom", nhom.getTenNhom());
        accessClaims.put("mach", mach);

        return jwtTokenProvider.generateAccessToken(accessClaims);
    }

    /**
     * GET /auth/me
     * Lấy thông tin user từ matk (đã được JWT filter gắn vào principal).
     */
    public MeResponse getMe(Long matk) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByMaTkWithDetails(matk)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        NhanVien nv = taiKhoan.getNhanVien();
        Nhom nhom = taiKhoan.getNhom();
        CuaHang ch = nv.getCuaHang();
        Long mach = (ch != null) ? ch.getMaCh() : null;

        MeResponse.NhanVienInfo nvInfo = MeResponse.NhanVienInfo.builder()
                .hoten(nv.getHoTen())
                .chucvu(nv.getChucVu())
                .mach(mach)
                .sdt(nv.getSdt())
                .diachi(nv.getDiaChi())
                .ngaysinh(nv.getNgaySinh())
                .gioitinh(nv.getGioiTinh())
                .build();

        return MeResponse.builder()
                .matk(taiKhoan.getMaTk())
                .manv(nv.getMaNv())
                .manhom(nhom.getMaNhom())
                .tennhom(nhom.getTenNhom())
                .nhanvien(nvInfo)
                .build();
    }

    /**
     * POST /auth/reset-password
     * Đổi mật khẩu: kiểm tra mật khẩu cũ → hash mật khẩu mới → cập nhật DB.
     */
    @Transactional
    public void resetPassword(Long matk, ResetPasswordRequest request) {
        // 1. Validate mật khẩu mới
        if (request.getNewPassword().length() < 6) {
            throw new AppException(ErrorCode.PASSWORD_TOO_SHORT);
        }

        // 2. Lấy tài khoản
        TaiKhoan taiKhoan = taiKhoanRepository.findById(matk)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 3. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), taiKhoan.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // 4. Hash và cập nhật mật khẩu mới
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        taiKhoan.setPassword(hashedPassword);
        taiKhoanRepository.save(taiKhoan);
    }

    // ==================== HELPER ====================

    private void checkAccountStatus(TaiKhoan taiKhoan) {
        String status = taiKhoan.getTrangThai();
        if ("KhoaCung".equals(status)) {
            throw new AppException(ErrorCode.ACCOUNT_LOCKED_PERMANENT);
        }
        if ("KhoaTam".equals(status)) {
            throw new AppException(ErrorCode.ACCOUNT_LOCKED_TEMP);
        }
    }
}
