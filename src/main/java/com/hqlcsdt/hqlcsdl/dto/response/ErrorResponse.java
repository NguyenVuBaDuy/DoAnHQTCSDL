package com.hqlcsdt.hqlcsdl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    
    @Builder.Default
    private boolean success = false;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }
}
