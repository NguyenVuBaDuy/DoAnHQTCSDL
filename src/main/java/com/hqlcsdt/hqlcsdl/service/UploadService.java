package com.hqlcsdt.hqlcsdl.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hqlcsdt.hqlcsdl.config.CloudinaryProperties;
import com.hqlcsdt.hqlcsdl.dto.response.UploadImageResponse;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Cloudinary cloudinary;
    private final CloudinaryProperties cloudinaryProperties;

    public UploadImageResponse uploadProductImage(MultipartFile file) {
        validateFile(file);

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", cloudinaryProperties.getFolder(),
                            "resource_type", "image",
                            "use_filename", true,
                            "unique_filename", true,
                            "overwrite", false
                    )
            );

            return UploadImageResponse.builder()
                    .url((String) uploadResult.get("secure_url"))
                    .publicId((String) uploadResult.get("public_id"))
                    .width(asInteger(uploadResult.get("width")))
                    .height(asInteger(uploadResult.get("height")))
                    .format((String) uploadResult.get("format"))
                    .build();
        } catch (IOException ex) {
            log.error("Cloudinary upload failed", ex);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ex) {
            log.error("Cloudinary delete failed for publicId={}", publicId, ex);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private Integer asInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }
}
