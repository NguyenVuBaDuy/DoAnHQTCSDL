package com.hqlcsdt.hqlcsdl.exception;

import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import lombok.Getter;

/**
 * Exception tùy chỉnh cho toàn bộ ứng dụng.
 * Chứa ErrorCode để GlobalExceptionHandler trả về response chuẩn.
 */
@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
