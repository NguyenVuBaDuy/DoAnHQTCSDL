package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.CuaHangRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.entity.CuaHang;
import com.hqlcsdt.hqlcsdl.service.CuaHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/cuahang")
@RequiredArgsConstructor
@Tag(name = "Cửa hàng", description = "Các API quản lý thông tin cửa hàng")
public class CuaHangController {

    private final CuaHangService cuaHangService;

    /**
     * GET /cuahang — Lấy danh sách tất cả cửa hàng
     */
    @Operation(summary = "Lấy tất cả cửa hàng", description = "Lấy danh sách tất cả các cửa hàng")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CuaHang>>> getAllCuaHang() {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getAllCuaHang()));
    }

    /**
     * GET /cuahang/{id} — Lấy thông tin cửa hàng theo mã
     */
    @Operation(summary = "Lấy chi tiết cửa hàng", description = "Lấy thông tin chi tiết cửa hàng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuaHang>> getCuaHangById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getCuaHangById(id)));
    }

    /**
     * POST /cuahang — Thêm cửa hàng mới
     */
    @Operation(summary = "Thêm cửa hàng", description = "Thêm một cửa hàng mới vào hệ thống")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createCuaHang(@Valid @RequestBody CuaHangRequest request) {
        cuaHangService.createCuaHang(request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thêm cửa hàng thành công")));
    }

    /**
     * PUT /cuahang/{id} — Cập nhật thông tin cửa hàng
     */
    @Operation(summary = "Cập nhật cửa hàng", description = "Cập nhật thông tin của cửa hàng theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateCuaHang(@PathVariable Long id, @Valid @RequestBody CuaHangRequest request) {
        cuaHangService.updateCuaHang(id, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật cửa hàng thành công")));
    }

    /**
     * DELETE /cuahang/{id} — Xóa cửa hàng
     */
    @Operation(summary = "Xóa cửa hàng", description = "Xóa cửa hàng khỏi hệ thống theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteCuaHang(@PathVariable Long id) {
        cuaHangService.deleteCuaHang(id);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa cửa hàng thành công")));
    }

    /**
     * PATCH /cuahang/{id}/status — Thay đổi trạng thái cửa hàng
     * Body: { "trangThai": "HoatDong" | "DongCua" | "TamNgung" }
     */
    @Operation(summary = "Thay đổi trạng thái cửa hàng", description = "Thay đổi trạng thái hoạt động của cửa hàng")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MessageResponse>> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("trangThai");
        cuaHangService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thay đổi trạng thái thành công")));
    }
}
