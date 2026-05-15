package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.request.KhachHangRequest;
import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.entity.KhachHang;
import com.hqlcsdt.hqlcsdl.service.KhachHangService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/khachhang")
@RequiredArgsConstructor
@Tag(name = "Khách hàng", description = "Các API quản lý thông tin khách hàng")
public class KhachHangController {

    private final KhachHangService khachHangService;

    @Operation(summary = "Lấy tất cả khách hàng", description = "Lấy danh sách tất cả các khách hàng")
    @GetMapping
    public ResponseEntity<ApiResponse<List<KhachHang>>> getAllKhachHang() {
        return ResponseEntity.ok(ApiResponse.success(khachHangService.getAllKhachHang()));
    }

    @Operation(summary = "Lấy chi tiết khách hàng", description = "Lấy thông tin chi tiết khách hàng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHang>> getKhachHangById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(khachHangService.getKhachHangById(id)));
    }

    @Operation(summary = "Thêm khách hàng", description = "Thêm một khách hàng mới vào hệ thống")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> createKhachHang(@Valid @RequestBody KhachHangRequest request) {
        khachHangService.createKhachHang(request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Thêm khách hàng thành công")));
    }

    @Operation(summary = "Cập nhật khách hàng", description = "Cập nhật thông tin của khách hàng theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateKhachHang(@PathVariable Long id, @Valid @RequestBody KhachHangRequest request) {
        khachHangService.updateKhachHang(id, request);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Cập nhật khách hàng thành công")));
    }

    @Operation(summary = "Xóa khách hàng", description = "Xóa khách hàng khỏi hệ thống theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteKhachHang(@PathVariable Long id) {
        khachHangService.deleteKhachHang(id);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa khách hàng thành công")));
    }
}
