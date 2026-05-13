package com.hqlcsdt.hqlcsdl.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hqlcsdt.hqlcsdl.dto.response.ErrorResponse;
import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter đọc JWT từ header Authorization: Bearer <token>,
 * verify và gắn authentication vào SecurityContext.
 *
 * Tương đương middleware verifyToken trong yêu cầu.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {
                Claims claims = jwtTokenProvider.parseAccessToken(token);

                Long matk = claims.get("matk", Long.class);
                String manv = claims.get("manv", String.class);
                Long manhom = claims.get("manhom", Long.class);
                String tennhom = claims.get("tennhom", String.class);
                Long mach = claims.get("mach", Long.class);

                // Tạo principal chứa toàn bộ thông tin cần thiết
                JwtUserPrincipal principal = new JwtUserPrincipal(matk, manv, manhom, tennhom, mach);

                // Gán role từ tennhom (VD: ROLE_Admin, ROLE_QuanLyCuaHang)
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + tennhom)
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (AppException ex) {
                // Token không hợp lệ / hết hạn → trả lỗi JSON ngay, không đi tiếp
                sendErrorResponse(response, ex.getErrorCode());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = new ErrorResponse(errorCode.getMessage(), errorCode.getCode());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
