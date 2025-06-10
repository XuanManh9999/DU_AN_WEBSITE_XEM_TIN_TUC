package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.UserRequest;
import com.datn.website_xem_tin_tuc.service.ManageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return manageUserService.deleteUser(userId);
    }

}
