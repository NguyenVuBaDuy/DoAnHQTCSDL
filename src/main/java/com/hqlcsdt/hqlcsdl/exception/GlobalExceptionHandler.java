package com.hqlcsdt.hqlcsdl.exception;

import com.hqlcsdt.hqlcsdl.dto.response.ErrorResponse;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Xử lý tập trung tất cả exception, trả về { message, code } + HTTP status.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse body = new ErrorResponse(ex.getMessage(), errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dữ liệu đầu vào không hợp lệ");

        ErrorResponse body = new ErrorResponse(message, ErrorCode.INVALID_INPUT.getCode());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Lỗi không mong muốn: ", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse body = new ErrorResponse(errorCode.getMessage(), errorCode.getCode());
        return ResponseEntity.status(500).body(body);
    }
}
