package com.eggplant.backend.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    private final String secretKey = "OQ2dPQJ9GDoNf0whfnN83as9UURfJXm6+dOwClGckXM="; // 반드시 환경 변수 또는 설정 파일로 관리하세요.
    private final long expirationTime = 1000 * 60 * 60; // 1시간 (밀리초)

    /**
     * JWT 토큰 생성 메서드
     * @param username 사용자 이름 (주로 토큰의 주체)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름(주체) 추출 메서드
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * JWT 토큰 유효성 검증 메서드
     * @param token JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 클레임 추출
     * @param token JWT 토큰
     * @return 클레임 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}