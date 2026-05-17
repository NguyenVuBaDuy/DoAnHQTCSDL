package com.hqlcsdt.hqlcsdl.controller;

import com.hqlcsdt.hqlcsdl.dto.response.ApiResponse;
import com.hqlcsdt.hqlcsdl.dto.response.MessageResponse;
import com.hqlcsdt.hqlcsdl.dto.response.UploadImageResponse;
import com.hqlcsdt.hqlcsdl.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "Upload ảnh lên Cloudinary")
public class UploadController {

    private final UploadService uploadService;

    /**
     * POST /upload/image — multipart field name: {@code file}
     * Trả về URL lưu vào SANPHAM.ANH khi tạo/cập nhật sản phẩm.
     */
    @Operation(summary = "Upload ảnh sản phẩm", description = "Upload file ảnh (JPEG/PNG/WebP, tối đa 5MB) lên Cloudinary")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadImageResponse>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(uploadService.uploadProductImage(file)));
    }

    @Operation(summary = "Xóa ảnh trên Cloudinary", description = "Xóa theo publicId trả về từ API upload")
    @DeleteMapping("/image")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteImage(@RequestParam String publicId) {
        uploadService.deleteImage(publicId);
        return ResponseEntity.ok(ApiResponse.success(new MessageResponse("Xóa ảnh thành công")));
    }
}
