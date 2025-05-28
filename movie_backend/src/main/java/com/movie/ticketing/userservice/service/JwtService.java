package com.movie.ticketing.userservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(Long userId, String username) {
        // 使用标准方法生成安全的512位密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 自动生成符合HS512要求的密钥

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key) // 直接使用生成的密钥，无需指定算法
                .compact();
    }
}