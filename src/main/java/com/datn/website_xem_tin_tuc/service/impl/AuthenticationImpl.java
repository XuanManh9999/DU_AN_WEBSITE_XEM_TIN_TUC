package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.EmailSending;
import com.datn.website_xem_tin_tuc.component.RandomStringGenerator;
import com.datn.website_xem_tin_tuc.dto.request.*;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.TokenResponse;
import com.datn.website_xem_tin_tuc.entity.*;
import com.datn.website_xem_tin_tuc.enums.Active;
import com.datn.website_xem_tin_tuc.enums.Role;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.InvalidDataNotFound;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.*;
import com.datn.website_xem_tin_tuc.service.AuthenticationService;
import com.datn.website_xem_tin_tuc.service.JwtService;
import com.datn.website_xem_tin_tuc.service.ManageUserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.datn.website_xem_tin_tuc.enums.TokenType.REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final ManageUserService manageUserService;
    private final JwtService jwtService;
    private final UserRepository userEntityRepository;
    private final EmailSending emailSending;
    private final RandomStringGenerator randomStringGenerator;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final FirebaseAuth firebaseAuth;
    private final AuthProviderRepository authProviderRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;


    @Override
    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        var user = manageUserService.findUserByUsername(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User or password is not found "));
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateTokenRefreshToken(user);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        // validate
        String token = request.getHeader("x-token");
        if (StringUtils.isBlank(token)) {
            log.error("Token must be not blank");
            return null;
        }
        // extract user for token
        final String username = jwtService.extractUsername(token, REFRESH_TOKEN);


        // check it into db
        var user = manageUserService.findUserByUsername(username).orElseThrow(() -> new InvalidDataNotFound("User not found"));
        if (!jwtService.isValid(token, REFRESH_TOKEN, user)) {
            throw new InvalidDataNotFound("Refresh token is invalid");
        }
        String assessToken = jwtService.generateAccessToken(user);
        return TokenResponse.builder()
                .accessToken(assessToken)
                .build();
    }
    @Transactional
    @Override
    public CommonResponse registerUser(RegisterRequest registerRequest) {
        try {
            LocalDateTime now = LocalDateTime.now();

            Optional<UserEntity> userEntity = userEntityRepository.findByUsernameAndEmail(
                    registerRequest.getUserName(), registerRequest.getEmail());
            Optional<UserEntity> userEntity_email = userEntityRepository.findByEmail(registerRequest.getEmail());
            Optional<UserEntity> userEntity_username = userEntityRepository.findByUsername(registerRequest.getUserName());

            // Trường hợp username đã tồn tại và đang hoạt động
            if (userEntity_username.isPresent() && userEntity_username.get().getActive() == Active.HOAT_DONG) {
                throw new DuplicateResourceException("Tên tài khoản đã tồn tại.");
            }

            // Trường hợp username + email đều tồn tại nhưng bị khóa
            if (userEntity.isPresent() && userEntity.get().getActive() == Active.BI_KHOA) {
                throw new DuplicateResourceException("Tài khoản đã tồn tại trong hệ thống nhưng đang bị khóa.");
            }

            // Trường hợp email đã tồn tại với trạng thái hoạt động (đăng nhập bằng cách khác)
            if (userEntity_email.isPresent() && userEntity_email.get().getActive() == Active.HOAT_DONG) {
                throw new DuplicateResourceException("Tài khoản đã tồn tại trên hệ thống với một phương thức đăng nhập khác.");
            }

            // ✅ Nếu email đã tồn tại nhưng chưa hoạt động → gửi lại OTP
            if (userEntity_email.isPresent() && userEntity_email.get().getActive() == Active.CHUA_HOAT_DONG) {
                // Xoá OTP cũ (nếu có)
                    otpRepository.deleteByEmail(registerRequest.getEmail());

                // Gửi OTP mới
                String encode = randomStringGenerator.generateRandomString(6);
                OtpEntity otp = new OtpEntity();
                otp.setEmail(registerRequest.getEmail());
                otp.setOtpCode(encode);
                otp.setExpiresAt(now.plusSeconds(120));
                otpRepository.save(otp);

                // Gửi mail
                emailSending.sendEmail(registerRequest.getEmail(), "Tin tức News 24h", "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; background: #f9f9f9;'>"
                        + "<h2 style='color: #333;'>🔑 Xác nhận OTP của bạn</h2>"
                        + "<p>Chào bạn,</p>"
                        + "<p>Đây là mã OTP của bạn để xác nhận: <strong style='font-size: 18px; color: #d9534f;'>" + encode + "</strong></p>"
                        + "<p>Mã OTP này chỉ có hiệu lực trong <strong>2 phút</strong>. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                        + "<hr style='border: none; border-top: 1px solid #ddd;'/>"
                        + "<p style='font-size: 12px; color: #666;'>Nếu bạn không yêu cầu OTP này, vui lòng bỏ qua email này.</p>"
                        + "<p style='font-size: 12px; color: #666;'>Cảm ơn bạn,<br/>Trang tin tức hàng đầu Việt Nam - Cập nhật tin tức nhanh nhất, chính xác nhất từ trong nước và thế giới.</p>"
                        + "</div>");

                return CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Tài khoản đã tồn tại nhưng chưa xác thực. Mã OTP mới đã được gửi lại.")
                        .build();
            }

            // ✅ Trường hợp hoàn toàn mới: tạo user mới
            UserEntity user = new UserEntity();
            user.setUsername(registerRequest.getUserName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setActive(Active.CHUA_HOAT_DONG);

            // Lưu user
            userEntityRepository.save(user);

            // Gán role mặc định là ROLE_USER
            Optional<RoleEntity> roleEntity = roleRepository.findRoleEntityByName(Role.ROLE_USER);
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserId(user);
            userRole.setRoleId(roleEntity.get());
            userRole.setActive(Active.HOAT_DONG);
            userRoleRepository.save(userRole);

            // Gửi OTP mới
            String encode = randomStringGenerator.generateRandomString(6);
            OtpEntity otp = new OtpEntity();
            otp.setEmail(registerRequest.getEmail());
            otp.setOtpCode(encode);
            otp.setExpiresAt(now.plusSeconds(120));
            otpRepository.save(otp);

            emailSending.sendEmail(registerRequest.getEmail(), "Tin tức News 24h", "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; background: #f9f9f9;'>"
                    + "<h2 style='color: #333;'>🔑 Xác nhận OTP của bạn</h2>"
                    + "<p>Chào bạn,</p>"
                    + "<p>Đây là mã OTP của bạn để xác nhận: <strong style='font-size: 18px; color: #d9534f;'>" + encode + "</strong></p>"
                    + "<p>Mã OTP này chỉ có hiệu lực trong <strong>2 phút</strong>. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                    + "<hr style='border: none; border-top: 1px solid #ddd;'/>"
                    + "<p style='font-size: 12px; color: #666;'>Nếu bạn không yêu cầu OTP này, vui lòng bỏ qua email này.</p>"
                    + "<p style='font-size: 12px; color: #666;'>Cảm ơn bạn,<br/>Trang tin tức hàng đầu Việt Nam - Cập nhật tin tức nhanh nhất, chính xác nhất từ trong nước và thế giới.</p>"
                    + "</div>");

            return CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác thực OTP.")
                    .build();

        } catch (Exception e) {
            throw e; // Có thể custom lại exception cho gọn
        }
    }

    @Override
    public CommonResponse forgotPassworrdUser(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Optional<UserEntity> user = userEntityRepository.findByEmail(forgotPasswordRequest.getEmail());
            if (user.isPresent()) {
                String encode = randomStringGenerator.generateRandomString(6);

                OtpEntity otp = new OtpEntity();
                otp.setEmail(forgotPasswordRequest.getEmail());
                otp.setOtpCode(encode);
                otp.setExpiresAt(now.plusSeconds(120));
                otpRepository.save(otp);
                emailSending.sendEmail(forgotPasswordRequest.getEmail(), "IMGBB OTP", "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; background: #f9f9f9;'>"
                        + "<h2 style='color: #333;'>🔑 Xác nhận OTP của bạn</h2>"
                        + "<p>Chào bạn,</p>"
                        + "<p>Đây là mã OTP của bạn để xác nhận: <strong style='font-size: 18px; color: #d9534f;'>" + encode + "</strong></p>"
                        + "<p>Mã OTP này chỉ có hiệu lực trong <strong>2 phút</strong>. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                        + "<hr style='border: none; border-top: 1px solid #ddd;'/>"
                        + "<p style='font-size: 12px; color: #666;'>Nếu bạn không yêu cầu OTP này, vui lòng bỏ qua email này.</p>"
                        + "<p style='font-size: 12px; color: #666;'>Cảm ơn bạn,<br/>Hệ thống lưu trữ file hàng đầu Việt Nam</p>"
                        + "</div>");
                return CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Forgot password otp successfully")
                        .build();
            }else {
                throw new NotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponse> verifyOtpForgotPassword(VerifyOtpRequest verifyOtpRequest) {
        try {
            boolean isCheckOtp = verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());
            if (isCheckOtp) {
                Optional<UserEntity> user = userEntityRepository.findByEmail(verifyOtpRequest.getEmail());
                if (user.isPresent()) {
                    String passwordNew = randomStringGenerator.generateRandomString(10);
                    user.get().setPassword(passwordEncoder.encode(passwordNew));
                    userEntityRepository.save(user.get());
                    emailSending.sendEmail(verifyOtpRequest.getEmail(), "New Password", "<div style=\"font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; background: #f9f9f9;\">\n" +
                            "    <h2 style=\"color: #333;\">\uD83D\uDD11 Mật khẩu mới của bạn</h2>\n" +
                            "    <p>Chào bạn,</p>\n" +
                            "    <p>Chúng tôi đã tạo một mật khẩu mới cho tài khoản của bạn:</p>\n" +
                            "    <p style=\"font-size: 18px; font-weight: bold; color: #d9534f;\">"+ passwordNew  + "</p>\n" +
                            "    <p>Vui lòng đăng nhập và thay đổi mật khẩu ngay để bảo mật tài khoản.</p>\n" +
                            "    <hr style=\"border: none; border-top: 1px solid #ddd;\">\n" +
                            "    <p style=\"font-size: 12px; color: #666;\">Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>\n" +
                            "    <p style=\"font-size: 12px; color: #666;\">Hệ thống lưu trữ file hàng đầu Việt Nam</p>\n" +
                            "</div>\n");
                    return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Verify OTP successfully")
                            .build());
                }else {
                    throw new NotFoundException("User not found");
                }

            }else {
                return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Verify OTP failed")
                        .build());
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponse> verifyOtpRegisteredUser(VerifyOtpRequest verifyOtpRequest) {
        try {
            boolean isCheckOtp = verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());
            if (isCheckOtp) {
                Optional<UserEntity> user = userEntityRepository.findUserEntityByEmailAndActive(verifyOtpRequest.getEmail(), Active.CHUA_HOAT_DONG);

                if (user.isPresent()) {
                    user.get().setActive(Active.HOAT_DONG);
                    userEntityRepository.save(user.get());
                    return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Verify OTP successfully")
                            .build());
                }else {
                    throw new NotFoundException("User not found");
                }
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Verify OTP failed")
                        .build());
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public FirebaseToken verifyToken(String token) {
        try {
            return firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            System.err.println("Invalid Token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<?> handleLoginOauth(OauthFireBaseRequest oauthFireBaseRequest) {
        try {
            Optional<UserEntity> userLocked = userEntityRepository.findUserEntityByEmailAndActive(oauthFireBaseRequest.getEmail(), Active.CHUA_HOAT_DONG);
            if (userLocked.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        CommonResponse.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .message("Đăng nhập không thành công, tài khoản đã tồn tại nhưng đang bị khóa")
                                .build()
                );
            }
            Optional<UserEntity> user = userEntityRepository.findUserEntityByEmailAndActive(oauthFireBaseRequest.getEmail(), Active.HOAT_DONG);



            if (user.isPresent()) {
                Optional<AuthProvider> authProviderLocked = authProviderRepository.findAuthProviderByUserAndActive(user.get(), Active.CHUA_HOAT_DONG);
                if (authProviderLocked.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            CommonResponse.builder()
                                    .status(HttpStatus.CONFLICT.value())
                                    .message("Đăng nhập không thành công, tài khoản đã tồn tại nhưng đang bị khóa")
                                    .build()
                    );
                }
                Optional<AuthProvider> authProviderActive = authProviderRepository.findAuthProviderByUserAndActive(user.get(), Active.HOAT_DONG);
                if (authProviderActive.isPresent()) {
                    String accessToken = jwtService.generateAccessToken(user.get());
                    String refreshToken = jwtService.generateTokenRefreshToken(user.get());
                    return ResponseEntity.status(HttpStatus.OK).body(TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .userId(user.get().getId())
                            .status(HttpStatus.OK.value())
                            .build());

                }else {
                    // liên kết tài khoản
                    AuthProvider authProvider = new AuthProvider();
                    authProvider.setUser(user.get());
                    authProvider.setProviderName(oauthFireBaseRequest.getProvider());
                    authProvider.setActive(Active.HOAT_DONG);
                    authProviderRepository.save(authProvider);

                    String accessToken = jwtService.generateAccessToken(user.get());
                    String refreshToken = jwtService.generateTokenRefreshToken(user.get());
                    return ResponseEntity.status(HttpStatus.OK).body(TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .userId(user.get().getId())
                            .status(HttpStatus.OK.value())
                            .build());
                }
            }else {
                UserEntity userEntity = new UserEntity();
                userEntity.setEmail(oauthFireBaseRequest.getEmail());
                userEntity.setAvatar(oauthFireBaseRequest.getAvatar());
                userEntity.setUsername(oauthFireBaseRequest.getUsername());
                userEntity.setActive(Active.HOAT_DONG);
                userEntityRepository.save(userEntity);
                AuthProvider authProvider = new AuthProvider();
                authProvider.setUser(userEntity);
                authProvider.setProviderName(oauthFireBaseRequest.getProvider());
                authProvider.setActive(Active.HOAT_DONG);

                Optional<RoleEntity> role = roleRepository.findRoleEntityByName(Role.ROLE_USER);
                if (!role.isPresent()) {
                    throw new NotFoundException("Error Role not found");
                }
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setUserId(userEntity);
                userRoleEntity.setRoleId(role.get());
                userRoleEntity.setActive(Active.HOAT_DONG);
                userRoleRepository.save(userRoleEntity);

                String accessToken = jwtService.generateAccessToken(userEntity);
                String refreshToken = jwtService.generateTokenRefreshToken(userEntity);
                return ResponseEntity.status(HttpStatus.OK).body(TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userId(userEntity.getId())
                        .status(HttpStatus.OK.value())
                        .build());
            }
        }catch (Exception e) {
            throw e;
        }
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<OtpEntity> otpEntityOpt = otpRepository.findOtpEntitiesByEmailAndOtpCode(email, otp);

        if (otpEntityOpt.isPresent()) {
            OtpEntity otpEntity = otpEntityOpt.get();

            // Kiểm tra nếu OTP đã hết hạn
            if (LocalDateTime.now().isAfter(otpEntity.getExpiresAt())) {
                otpRepository.delete(otpEntity); // Xóa OTP hết hạn
                return false; // OTP hết hạn
            }

            if (otpEntity.getOtpCode().equals(otp)) {
                return true;
            }
        }
        return false;
    }
}