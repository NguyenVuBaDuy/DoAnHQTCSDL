package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.NhaCungCapRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.entity.NhaCungCap;
import com.hqlcsdt.hqlcsdl.service.NhaCungCapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/nhacungcap")
@RequiredArgsConstructor
@Tag(name = "Nhà cung cấp", description = "Các API quản lý thông tin nhà cung cấp")
public class NhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    /**
     * GET /nhacungcap — Lấy danh sách tất cả nhà cung cấp
     */
    @Operation(summary = "Lấy tất cả nhà cung cấp", description = "Lấy danh sách tất cả các nhà cung cấp")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NhaCungCap>>> getAllNhaCungCap() {
        return ResponseEntity.ok(ApiResponse.success(nhaCungCapService.getAllNhaCungCap()));
    }

    /**
     * GET /nhacungcap/{id} — Lấy thông tin nhà cung cấp theo mã
     */
    @Operation(summary = "Lấy chi tiết nhà cung cấp", description = "Lấy thông tin chi tiết nhà cung cấp theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NhaCungCap>> getNhaCungCapById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(nhaCungCapService.getNhaCungCapById(id)));
    }

    /**
     * POST /nhacungcap — Thêm nhà cung cấp mới
     */
    @Operation(summary = "Thêm nhà cung cấp", description = "Thêm một nhà cung cấp mới vào hệ thống")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createNhaCungCap(       
            @Valid @RequestBody NhaCungCapRequest request) {
        nhaCungCapService.createNhaCungCap(request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thêm nhà cung cấp thành công")));
    }

    /**
     * PUT /nhacungcap/{id} — Cập nhật thông tin nhà cung cấp
     */
    @Operation(summary = "Cập nhật nhà cung cấp", description = "Cập nhật thông tin của nhà cung cấp theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateNhaCungCap(@PathVariable Long id,
            @Valid @RequestBody NhaCungCapRequest request) {
        nhaCungCapService.updateNhaCungCap(id, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật nhà cung cấp thành công")));
    }

    /**
     * DELETE /nhacungcap/{id} — Xóa nhà cung cấp
     */
    @Operation(summary = "Xóa nhà cung cấp", description = "Xóa nhà cung cấp khỏi hệ thống theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteNhaCungCap(@PathVariable Long id) {
        nhaCungCapService.deleteNhaCungCap(id);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa nhà cung cấp thành công")));
    }

    /**
     * PATCH /nhacungcap/{id}/status — Thay đổi trạng thái nhà cung cấp
     * Body: { "trangThai": "HoatDong" | "DungHopTac" }
     */
    @Operation(summary = "Thay đổi trạng thái nhà cung cấp", description = "Thay đổi trạng thái hoạt động của nhà cung cấp")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MessageResponse>> changeStatus(@PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("trangThai");
        nhaCungCapService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thay đổi trạng thái thành công")));
    }
}
