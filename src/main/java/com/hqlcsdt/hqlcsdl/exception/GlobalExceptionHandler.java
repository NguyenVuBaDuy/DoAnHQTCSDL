package com.hqlcsdt.hqlcsdl.exception;

import com.hqlcsdt.hqlcsdl.dto.response.ErrorResponse;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * Xử lý tập trung tất cả exception, trả về { code, message, success, timestamp } + HTTP status.
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

    /**
     * Bắt lỗi từ Oracle Stored Procedure (RAISE_APPLICATION_ERROR → ORA-20xxx).
     * PersistenceException bọc bên ngoài SQLException chứa message của procedure.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException ex) {
        String message = extractOracleMessage(ex);
        log.warn("Stored procedure error: {}", message);
        ErrorResponse body = new ErrorResponse(message, "STORED_PROCEDURE_ERROR");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Lỗi không mong muốn: ", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse body = new ErrorResponse(errorCode.getMessage(), errorCode.getCode());
        return ResponseEntity.status(500).body(body);
    }

    // ==================== HELPER ====================

    /**
     * Trích xuất message từ Oracle RAISE_APPLICATION_ERROR.
     * Chuỗi gốc dạng: "ORA-20003: So dien thoai da ton tai\nORA-06512: ..."
     * → Trả về: "So dien thoai da ton tai"
     */
    private String extractOracleMessage(PersistenceException ex) {
        Throwable cause = ex;
        while (cause != null) {
            if (cause instanceof SQLException sqlEx) {
                String msg = sqlEx.getMessage();
                if (msg != null && msg.contains("ORA-20")) {
                    // Lấy dòng đầu tiên, bỏ prefix "ORA-20xxx: "
                    String firstLine = msg.split("\n")[0];
                    int colonIndex = firstLine.indexOf(": ");
                    if (colonIndex >= 0) {
                        return firstLine.substring(colonIndex + 2).trim();
                    }
                    return firstLine;
                }
            }
            cause = cause.getCause();
        }
        // Fallback nếu không tìm thấy ORA-20xxx
        return ex.getMessage() != null ? ex.getMessage() : "Lỗi thực thi stored procedure";
    }
}
