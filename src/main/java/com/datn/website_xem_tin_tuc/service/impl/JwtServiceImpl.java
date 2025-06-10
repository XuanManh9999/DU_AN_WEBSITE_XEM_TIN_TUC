package com.datn.website_xem_tin_tuc.service.impl;


import com.datn.website_xem_tin_tuc.enums.TokenType;
import com.datn.website_xem_tin_tuc.exceptions.customs.InvalidDataNotFound;
import com.datn.website_xem_tin_tuc.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.datn.website_xem_tin_tuc.enums.TokenType.ACCESS_TOKEN;
import static com.datn.website_xem_tin_tuc.enums.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiryHour}")
    private Long expiryHour;
    @Value("${jwt.expiryDay}")
    private Long expiryDay;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;


    @Override
    public String generateAccessToken(UserDetails userDetails) {
        // TO do xu ly tao ra token
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateTokenRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType tokenType, UserDetails userDetails) {
        final String username = extractUsername(token, tokenType);
        // can phai kiem tra xem con han ko
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType);
    }
    // Hàm kiểm tra xem token đã hết hạn chưa
    private boolean isTokenExpired(String token, TokenType tokenType) {
        Date expiration = extractExpiration(token, tokenType);
        return expiration.before(new Date());
    }

    // Hàm trích xuất thời gian hết hạn từ token
    private Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                // claims la ki tu bi mat ma khong hien thi trong token
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                // ngay tao ra token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                // claims la ki tu bi mat ma khong hien thi trong token
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                // ngay tao ra token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }
    public Key getKey(TokenType type) {
        byte[] keyBytes;
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataNotFound("Token type invalid");
        }
    }


    private <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token, tokenType);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType tokenType) {
        return Jwts.parserBuilder().setSigningKey(getKey(tokenType))
                .build().parseClaimsJws(token).getBody();
    }
}