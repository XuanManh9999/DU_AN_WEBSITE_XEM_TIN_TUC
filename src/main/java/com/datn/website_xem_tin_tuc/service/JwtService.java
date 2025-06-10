package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateTokenRefreshToken(UserDetails userDetails);
    String extractUsername(String token, TokenType tokenType);
    boolean isValid(String token, TokenType tokenType, UserDetails userDetails);
}
