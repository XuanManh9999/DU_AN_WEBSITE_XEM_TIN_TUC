package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.*;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.TokenResponse;
import com.datn.website_xem_tin_tuc.service.AuthenticationService;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/access")
    public ResponseEntity<TokenResponse> login (@RequestBody SignInRequest signInRequest)  {
        return new ResponseEntity<>(authenticationService.authenticate(signInRequest), HttpStatus.OK);
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh (HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refresh(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register (@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUser(registerRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword (@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.forgotPassworrdUser(forgotPasswordRequest));
    }

    @PostMapping("/verify-otp-register")
    public ResponseEntity<CommonResponse> verifyOtpRegister(@RequestBody VerifyOtpRequest verifyOtpRequest) {
        return authenticationService.verifyOtpRegisteredUser(verifyOtpRequest);
    }

    @PostMapping("/verify-otp-forgot-password")
    public ResponseEntity<CommonResponse> verifyOtpForgotPassword(@RequestBody VerifyOtpRequest verifyOtpRequest) {
        return authenticationService.verifyOtpForgotPassword(verifyOtpRequest);
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<?> LoginWithFireBase(@RequestBody OauthFireBaseRequest oauthFireBaseRequest) {
        if (StringUtils.isEmpty(oauthFireBaseRequest.getId_token())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("idtoken, email, provider is required")
                    .build());
        }
        FirebaseToken firebaseToken = authenticationService.verifyToken(oauthFireBaseRequest.getId_token());
        if (firebaseToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Token không hợp lệ")
                    .build());
        }
        return authenticationService.handleLoginOauth(oauthFireBaseRequest);
    }

}