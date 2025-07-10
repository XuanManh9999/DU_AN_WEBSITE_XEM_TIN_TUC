package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.ChangePasswordRequest;
import com.datn.website_xem_tin_tuc.dto.request.UserRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.service.ManageUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final ManageUserService manageUserService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers (
            @RequestParam(defaultValue = "10", name = "limit") Integer limit,
            @RequestParam(defaultValue = "0", name = "limit") Integer offset
    ) {
        return manageUserService.getAllUsers(limit, offset);
    }

    @GetMapping("/current")
    public ResponseEntity<CommonResponse> getCurrentUserByToken (
            HttpServletRequest request
    ) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .build());
        }

        return manageUserService.getCurrentUser(token);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "userId") Long userId) {
        return manageUserService.getUserById(userId);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        return manageUserService.addUser(userRequest);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId ,@RequestBody UserRequest userRequest) {
        return manageUserService.updateUser(userRequest, userId);
    }



    @PutMapping("/change-password")
    public ResponseEntity<?> changePasswordUser(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return manageUserService.changePasswordUser(changePasswordRequest);
    }

    @PutMapping("/change-info")
    public ResponseEntity<?> changePassword(
            @RequestParam(required = false) String username,
            @RequestParam (required = false) MultipartFile avatar
    ) {
        return manageUserService.updateInfoUser(username, avatar);
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return manageUserService.deleteUser(userId);
    }



}
