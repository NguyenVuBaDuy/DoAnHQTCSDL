package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.LoginRequest;
import com.hqlcsdt.hqlcsdl.dto.request.RefreshTokenRequest;
import com.hqlcsdt.hqlcsdl.dto.request.ResetPasswordRequest;
import com.hqlcsdt.hqlcsdl.dto.response.LoginResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MeResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.security.JwtUserPrincipal;
import com.hqlcsdt.hqlcsdl.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /auth/login
     * Input: { manv, password }
     * Output: { accessToken, refreshToken, user: { matk, manv, hoten, manhom, tennhom, chucvu, mach } }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/refresh
     * Input: { refreshToken }
     * Output: { accessToken }
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String accessToken = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    /**
     * POST /auth/logout
     * Stateless — server không cần làm gì.
     * Frontend tự xóa accessToken + refreshToken khỏi localStorage.
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công"));
    }

    /**
     * GET /auth/me
     * Header: Authorization: Bearer <accessToken>
     * Output: { matk, manv, manhom, tennhom, nhanvien: { hoten, chucvu, mach, sdt, diachi, ngaysinh, gioitinh } }
     */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> getMe(@AuthenticationPrincipal JwtUserPrincipal principal) {
        MeResponse response = authService.getMe(principal.getMatk());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/reset-password
     * Header: Authorization: Bearer <accessToken>
     * Input: { oldPassword, newPassword }
     * Output: { message: 'Đổi mật khẩu thành công' }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(principal.getMatk(), request);
        return ResponseEntity.ok(new MessageResponse("Đổi mật khẩu thành công"));
    }
}
