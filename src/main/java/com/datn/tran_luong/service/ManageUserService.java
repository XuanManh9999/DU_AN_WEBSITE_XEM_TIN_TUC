package com.datn.tran_luong.service;

import com.datn.tran_luong.dto.request.UserRequest;
import com.datn.tran_luong.dto.response.CommonResponse;
import com.datn.tran_luong.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface ManageUserService {
    UserDetailsService userDetailsService();
    Optional<UserEntity> findUserByUsername(String username);
    ResponseEntity<CommonResponse> getAllUsers(Integer limit, Integer offset);
    ResponseEntity<CommonResponse> getUserById(Long userId);
    ResponseEntity<CommonResponse> addUser(UserRequest userRequest);
    ResponseEntity<CommonResponse> updateUser(UserRequest userRequest, Long userId);
    ResponseEntity<CommonResponse> deleteUser(Long userId);
    ResponseEntity<?> getAllRoles();
    ResponseEntity<?> getUserByName(String name);
}