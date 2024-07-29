package com.lec.spring.jwt;

// JWT '발급' 및 '검증'

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8)
                , Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //------------------------------------------------------------------
    // JWT 생성
    // Payload 에 저장될 정보
    // - id, username, role, 생성일, 만료일
    public String createJwt(Long id, String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis())) // 성성일
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료일시
                .signWith(secretKey)
                .compact();

    }

    //------------------------------------------------------------------
    // JWT token 에서 내용 확인
    public Integer getId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Integer.class);
    }

    public String getUsername(String token) {  // username 확인
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(String token) {  // role 확인

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {  // 만료일 확인

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}





















