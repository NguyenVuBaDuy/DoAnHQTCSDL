package com.hqlcsdt.hqlcsdl.enums;

import lombok.Getter;

/**
 * Enum chứa tất cả mã lỗi và message mặc định của hệ thống.
 * Mỗi ErrorCode gồm: code (String dùng cho frontend), message (tiếng Việt), httpStatus.
 */
@Getter
public enum ErrorCode {

    // === 400 Bad Request ===
    INVALID_INPUT("INVALID_INPUT", "Dữ liệu đầu vào không hợp lệ", 400),
    PASSWORD_TOO_SHORT("PASSWORD_TOO_SHORT", "Mật khẩu phải có tối thiểu 6 ký tự", 400),
    OLD_PASSWORD_INCORRECT("OLD_PASSWORD_INCORRECT", "Mật khẩu cũ không đúng", 400),

    // === 401 Unauthorized ===
    AUTHENTICATION_REQUIRED("AUTHENTICATION_REQUIRED", "Bạn cần đăng nhập", 401),
    WRONG_PASSWORD("WRONG_PASSWORD", "Sai mật khẩu", 401),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "Token đã hết hạn", 401),
    TOKEN_INVALID("TOKEN_INVALID", "Token không hợp lệ", 401),

    // === 403 Forbidden ===
    ACCESS_DENIED("ACCESS_DENIED", "Bạn không có quyền truy cập", 403),
    ACCOUNT_LOCKED_PERMANENT("ACCOUNT_LOCKED_PERMANENT", "Tài khoản đã bị khóa vĩnh viễn", 403),
    ACCOUNT_LOCKED_TEMP("ACCOUNT_LOCKED_TEMP", "Tài khoản đang tạm khóa", 403),

    // === 404 Not Found ===
    EMPLOYEE_NOT_FOUND("EMPLOYEE_NOT_FOUND", "Mã nhân viên không tồn tại", 404),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Tài khoản không tồn tại", 404),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Không tìm thấy tài nguyên", 404),

    // === 500 Internal Server Error ===
    INTERNAL_ERROR("INTERNAL_ERROR", "Lỗi hệ thống, vui lòng thử lại sau", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
