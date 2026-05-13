package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.LoginRequest;
import com.hqlcsdt.hqlcsdl.dto.request.RefreshTokenRequest;
import com.hqlcsdt.hqlcsdl.dto.request.ResetPasswordRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Xác thực", description = "Các API quản lý đăng nhập, cấp lại token và thông tin cá nhân")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /auth/login
     * Input: { manv, password }
     * Output: { accessToken, refreshToken, user: { matk, manv, hoten, manhom, tennhom, chucvu, mach } }
     */
    @Operation(summary = "Đăng nhập", description = "Xác thực và trả về Access Token, Refresh Token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /auth/refresh
     * Input: { refreshToken }
     * Output: { accessToken }
     */
    @Operation(summary = "Làm mới Token", description = "Dùng Refresh Token để lấy Access Token mới")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String accessToken = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", accessToken)));
    }

    /**
     * POST /auth/logout
     * Stateless — server không cần làm gì.
     * Frontend tự xóa accessToken + refreshToken khỏi localStorage.
     */
    @Operation(summary = "Đăng xuất", description = "Đăng xuất khỏi hệ thống (Chủ yếu gọi để xoá ở client)")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<MessageResponse>> logout() {
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Đăng xuất thành công")));
    }

    /**
     * GET /auth/me
     * Header: Authorization: Bearer <accessToken>
     * Output: { matk, manv, manhom, tennhom, nhanvien: { hoten, chucvu, mach, sdt, diachi, ngaysinh, gioitinh } }
     */
    @Operation(summary = "Lấy thông tin cá nhân", description = "Lấy thông tin của người dùng đang đăng nhập (từ Token)")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> getMe(@AuthenticationPrincipal JwtUserPrincipal principal) {
        MeResponse response = authService.getMe(principal.getMatk());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /auth/reset-password
     * Header: Authorization: Bearer <accessToken>
     * Input: { oldPassword, newPassword }
     * Output: { message: 'Đổi mật khẩu thành công' }
     */
    @Operation(summary = "Đổi mật khẩu", description = "Đổi mật khẩu của người dùng đang đăng nhập")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<MessageResponse>> resetPassword(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(principal.getMatk(), request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Đổi mật khẩu thành công")));
    }
}
