package com.datn.tran_luong.service;

import com.datn.tran_luong.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateTokenRefreshToken(UserDetails userDetails);
    String extractUsername(String token, TokenType tokenType);
    boolean isValid(String token, TokenType tokenType, UserDetails userDetails);
}
