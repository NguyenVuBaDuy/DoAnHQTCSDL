package com.hqlcsdt.hqlcsdl.security;

import com.hqlcsdt.hqlcsdl.enums.ErrorCode;
import com.hqlcsdt.hqlcsdl.exception.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Quản lý tạo và xác thực JWT token.
 * Tách biệt accessToken (ngắn hạn) và refreshToken (dài hạn) với secret key riêng.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access-token-expires}")
    private long accessTokenExpires;

    @Value("${jwt.refresh-token-expires}")
    private long refreshTokenExpires;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        // Pad key to at least 256 bits for HMAC-SHA256
        this.accessKey = Keys.hmacShaKeyFor(padSecret(accessTokenSecret));
        this.refreshKey = Keys.hmacShaKeyFor(padSecret(refreshTokenSecret));
    }

    /**
     * Tạo Access Token.
     * Payload: { matk, manv, manhom, tennhom, mach }
     */
    public String generateAccessToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpires);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(accessKey)
                .compact();
    }

    /**
     * Tạo Refresh Token.
     * Payload: { matk, manv }
     */
    public String generateRefreshToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpires);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(refreshKey)
                .compact();
    }

    /**
     * Verify và trả về payload của Access Token.
     */
    public Claims parseAccessToken(String token) {
        return parseToken(token, accessKey);
    }

    /**
     * Verify và trả về payload của Refresh Token.
     */
    public Claims parseRefreshToken(String token) {
        return parseToken(token, refreshKey);
    }

    private Claims parseToken(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException ex) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    /**
     * Pad secret string to minimum 32 bytes (256 bits) for HS256.
     */
    private byte[] padSecret(String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= 32) {
            return bytes;
        }
        byte[] padded = new byte[32];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }
}
