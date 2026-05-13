package com.hqlcsdt.hqlcsdl.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Principal object chứa thông tin user từ JWT payload.
 * Được gắn vào SecurityContext để các layer khác truy cập.
 *
 * Tương đương req.user trong yêu cầu:
 *   { matk, manv, manhom, tennhom, mach }
 */
@Getter
@AllArgsConstructor
public class JwtUserPrincipal {
    private Long matk;
    private String manv;
    private Long manhom;
    private String tennhom;
    private Long mach;
}
