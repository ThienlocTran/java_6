package com.java6.springboot.lab6.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    /**
     * Tạo JWT (JSON Web Token)
     *
     * @param user là UserDetails chứa thông tin để tạo token
     * @param expirySeconds là thời hạn có hiệu lực (tính bằng giây)
     */
    public String create(UserDetails user, int expirySeconds) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(Map.of("name", "Poly"))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * expirySeconds))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Bóc tách phần body từ JWT
     *
     * @param token là JWT
     */
    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey()) // xác minh token
                .build().parseClaimsJws(token).getBody();
    }
    /**
     * Xác minh thời gian hiệu lực
     *
     * @param claims là body của token
     */
    public boolean validate(Claims claims) {
        return claims.getExpiration().after(new Date());
    }
    /**
     * Tạo chữ ký số để ký và xác minh JWT
     */
    public   Key getSigningKey() {
        String secret = "0123456789.0123456789.0123456789"; // HS256 >= 32 byte
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
