package com.gdg.jwtexample.jwt;

import com.gdg.jwtexample.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessValidityMs;
    private final long refreshValidityMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessValidityMs,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshValidityMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessValidityMs = accessValidityMs;
        this.refreshValidityMs = refreshValidityMs;
    }

    // 액세스 토큰 발급 (UserRole 사용)
    public String createAccessToken(String email, UserRole role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessValidityMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", role.name()) // "ROLE_USER" 문자열로 저장
                .issuedAt(now)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 리프레시 토큰 발급
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshValidityMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Claims 추출
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 설정 값 조회용 getter
    public long getRefreshValidityMs() {
        return refreshValidityMs;
    }

    public long getAccessValidityMs() {
        return accessValidityMs;
    }
}
