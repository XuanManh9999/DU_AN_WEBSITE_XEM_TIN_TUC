package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.ChangePasswordRequest;
import com.datn.website_xem_tin_tuc.dto.request.UserRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ManageUserService {
    UserDetailsService userDetailsService();
    Optional<UserEntity> findUserByUsername(String username);
    ResponseEntity<CommonResponse> getAllUsers(Integer limit, Integer offset);
    ResponseEntity<CommonResponse> getUserById(Long userId);
    ResponseEntity<CommonResponse> addUser(UserRequest userRequest);
    ResponseEntity<CommonResponse> updateUser(UserRequest userRequest, Long userId);
    ResponseEntity<CommonResponse> deleteUser(Long userId);
    ResponseEntity<CommonResponse> getCurrentUser(String token);
    ResponseEntity<?> updateInfoUser(String username, MultipartFile avatar);
    ResponseEntity<?> changePasswordUser(ChangePasswordRequest changePasswordRequest);

}