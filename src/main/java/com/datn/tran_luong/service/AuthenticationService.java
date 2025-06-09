package com.datn.tran_luong.service;

import com.datn.tran_luong.dto.request.*;
import com.datn.tran_luong.dto.response.CommonResponse;
import com.datn.tran_luong.dto.response.TokenResponse;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest signInRequest);
    TokenResponse refresh(HttpServletRequest request);
    CommonResponse registerUser(RegisterRequest registerRequest);
    CommonResponse forgotPassworrdUser(ForgotPasswordRequest forgotPasswordRequest);
    ResponseEntity<CommonResponse> verifyOtpForgotPassword(VerifyOtpRequest verifyOtpRequest);
    ResponseEntity<CommonResponse> verifyOtpRegisteredUser(VerifyOtpRequest verifyOtpRequest);
    FirebaseToken verifyToken(String token);
    ResponseEntity<?> handleLoginOauth(OauthFireBaseRequest oauthFireBaseRequest);
}
