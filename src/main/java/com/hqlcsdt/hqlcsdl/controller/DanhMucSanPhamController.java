package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.DanhMucResponse;
import com.hqlcsdt.hqlcsdl.service.DanhMucSanPhamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/danhmuc")
@RequiredArgsConstructor
@Tag(name = "Danh mục sản phẩm", description = "Các API lấy thông tin danh mục sản phẩm")
public class DanhMucSanPhamController {

    private final DanhMucSanPhamService danhMucSanPhamService;

    /**
     * GET /danhmuc — Lấy danh sách tất cả danh mục sản phẩm
     */
    @Operation(summary = "Lấy tất cả danh mục", description = "Lấy danh sách tất cả các danh mục sản phẩm")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DanhMucResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(danhMucSanPhamService.getAllCategories()));
    }

    /**
     * GET /danhmuc/cay — Lấy danh sách danh mục sản phẩm theo cấu trúc cây (cha - con)
     */
    @Operation(summary = "Lấy danh mục dạng cây", description = "Lấy danh sách danh mục sản phẩm phân cấp cha - con")
    @GetMapping("/cay")
    public ResponseEntity<ApiResponse<List<DanhMucResponse>>> getCategoryTree() {
        return ResponseEntity.ok(ApiResponse.success(danhMucSanPhamService.getCategoryTree()));
    }
}
