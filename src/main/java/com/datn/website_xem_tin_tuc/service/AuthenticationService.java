package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.*;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.TokenResponse;
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
