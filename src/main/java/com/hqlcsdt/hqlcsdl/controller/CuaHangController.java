package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.CuaHangRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.entity.CuaHang;
import com.hqlcsdt.hqlcsdl.service.CuaHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuahang")
@RequiredArgsConstructor
public class CuaHangController {

    private final CuaHangService cuaHangService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CuaHang>>> getAllCuaHang() {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getAllCuaHang()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuaHang>> getCuaHangById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getCuaHangById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'QuanLyCuaHang')")
    public ResponseEntity<ApiResponse<CuaHang>> createCuaHang(@Valid @RequestBody CuaHangRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.createCuaHang(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'QuanLyCuaHang')")
    public ResponseEntity<ApiResponse<CuaHang>> updateCuaHang(@PathVariable Long id, @Valid @RequestBody CuaHangRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.updateCuaHang(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteCuaHang(@PathVariable Long id) {
        cuaHangService.deleteCuaHang(id);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa cửa hàng thành công")));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('Admin', 'QuanLyCuaHang')")
    public ResponseEntity<ApiResponse<MessageResponse>> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("trangThai");
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
        cuaHangService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thay đổi trạng thái thành công")));
    }
}
