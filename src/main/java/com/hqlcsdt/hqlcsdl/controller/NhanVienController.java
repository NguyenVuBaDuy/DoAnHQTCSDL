package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.NhanVienRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.dto.response.NhanVienResponse;
import com.hqlcsdt.hqlcsdl.security.JwtUserPrincipal;
import com.hqlcsdt.hqlcsdl.service.NhanVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.hqlcsdt.hqlcsdl.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/nhan-vien")
@RequiredArgsConstructor
@Tag(name = "Nhân viên", description = "Các API quản lý nhân viên và tài khoản")
public class NhanVienController {

    private final NhanVienService nhanVienService;

    /**
     * GET /nhan-vien
     * Lấy danh sách nhân viên có phân trang và filter
     */
    @Operation(summary = "Lấy tất cả nhân viên", description = "Lấy danh sách nhân viên có phân trang và filter")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NhanVienResponse>>> getAllNhanVien(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam(required = false) Long mach,
            @RequestParam(required = false) String chucvu,
            @RequestParam(required = false) String trangthai,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        
        Page<NhanVienResponse> result = nhanVienService.searchNhanVien(principal, mach, chucvu, trangthai, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(result)));
    }

    /**
     * GET /nhan-vien/{manv}
     * Lấy thông tin chi tiết nhân viên
     */
    @Operation(summary = "Lấy chi tiết nhân viên", description = "Lấy thông tin chi tiết nhân viên theo mã nhân viên")
    @GetMapping("/{manv}")
    public ResponseEntity<ApiResponse<NhanVienResponse>> getNhanVienById(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable String manv) {
        return ResponseEntity.ok(ApiResponse.success(nhanVienService.getDetailByMaNv(principal, manv)));
    }

    /**
     * POST /nhan-vien
     * Thêm nhân viên mới + tự động tạo TAIKHOAN
     */
    @Operation(summary = "Thêm nhân viên", description = "Thêm nhân viên mới và tự động tạo tài khoản tương ứng")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createNhanVien(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Valid @RequestBody NhanVienRequest request) {
        nhanVienService.createNhanVien(principal, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thêm nhân viên thành công")));
    }

    /**
     * PUT /nhan-vien/{manv}
     * Cập nhật thông tin nhân viên
     */
    @Operation(summary = "Cập nhật nhân viên", description = "Cập nhật thông tin nhân viên theo mã nhân viên")
    @PutMapping("/{manv}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateNhanVien(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable String manv,
            @Valid @RequestBody NhanVienRequest request) {
        nhanVienService.updateNhanVien(principal, manv, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật thông tin nhân viên thành công")));
    }

    /**
     * DELETE /nhan-vien/{manv}
     * Vô hiệu hóa nhân viên (TRANGTHAI = KhoaCung)
     */
    @Operation(summary = "Vô hiệu hóa nhân viên", description = "Chuyển trạng thái tài khoản của nhân viên thành khóa cứng")
    @DeleteMapping("/{manv}")
    public ResponseEntity<ApiResponse<MessageResponse>> disableNhanVien(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable String manv) {
        nhanVienService.disableNhanVien(principal, manv);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Vô hiệu hóa nhân viên thành công")));
    }

    /**
     * PUT /nhan-vien/{manv}/tai-khoan/role
     * Cập nhật nhóm quyền (Role) cho tài khoản
     */
    @Operation(summary = "Cập nhật quyền", description = "Thay đổi nhóm quyền cho tài khoản nhân viên")
    @PutMapping("/{manv}/tai-khoan/role")
    public ResponseEntity<ApiResponse<MessageResponse>> updateRole(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable String manv,
            @RequestBody java.util.Map<String, Long> body) {
        Long maNhom = body.get("maNhom");
        if (maNhom == null) {
            throw new IllegalArgumentException("maNhom không được để trống");
        }
        nhanVienService.updateRole(principal, manv, maNhom);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật quyền thành công")));
    }

    /**
     * PUT /nhan-vien/{manv}/tai-khoan/trang-thai
     * Cập nhật trạng thái tài khoản (HoatDong, KhoaCung, KhoaTam)
     */
    @Operation(summary = "Cập nhật trạng thái tài khoản", description = "Cập nhật trạng thái của tài khoản (HoatDong, KhoaCung, KhoaTam)")
    @PutMapping("/{manv}/tai-khoan/trang-thai")
    public ResponseEntity<ApiResponse<MessageResponse>> updateStatus(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @PathVariable String manv,
            @RequestBody java.util.Map<String, String> body) {
        String status = body.get("trangThai");
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("trangThai không được để trống");
        }
        nhanVienService.updateStatus(principal, manv, status);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật trạng thái thành công")));
    }
}
