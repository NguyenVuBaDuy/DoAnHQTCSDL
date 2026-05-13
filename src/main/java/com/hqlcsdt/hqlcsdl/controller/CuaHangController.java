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

@RestController
@RequestMapping("/cuahang")
@RequiredArgsConstructor
public class CuaHangController {

    private final CuaHangService cuaHangService;

    /**
     * GET /cuahang — Lấy danh sách tất cả cửa hàng
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CuaHang>>> getAllCuaHang() {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getAllCuaHang()));
    }

    /**
     * GET /cuahang/{id} — Lấy thông tin cửa hàng theo mã
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuaHang>> getCuaHangById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(cuaHangService.getCuaHangById(id)));
    }

    /**
     * POST /cuahang — Thêm cửa hàng mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createCuaHang(@Valid @RequestBody CuaHangRequest request) {
        cuaHangService.createCuaHang(request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thêm cửa hàng thành công")));
    }

    /**
     * PUT /cuahang/{id} — Cập nhật thông tin cửa hàng
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateCuaHang(@PathVariable Long id, @Valid @RequestBody CuaHangRequest request) {
        cuaHangService.updateCuaHang(id, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật cửa hàng thành công")));
    }

    /**
     * DELETE /cuahang/{id} — Xóa cửa hàng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteCuaHang(@PathVariable Long id) {
        cuaHangService.deleteCuaHang(id);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa cửa hàng thành công")));
    }

    /**
     * PATCH /cuahang/{id}/status — Thay đổi trạng thái cửa hàng
     * Body: { "trangThai": "HoatDong" | "DongCua" | "TamNgung" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MessageResponse>> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("trangThai");
        cuaHangService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thay đổi trạng thái thành công")));
    }
}
