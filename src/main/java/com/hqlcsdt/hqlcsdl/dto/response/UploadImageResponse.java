package com.hqlcsdt.hqlcsdl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageResponse {

    /** URL HTTPS dùng lưu vào cột SANPHAM.ANH */
    private String url;

    /** public_id trên Cloudinary — dùng khi xóa/thay ảnh */
    private String publicId;

    private Integer width;
    private Integer height;
    private String format;
}
