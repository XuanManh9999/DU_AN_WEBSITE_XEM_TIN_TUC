package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashBoardService dashBoardService;
    @GetMapping("/home")
    public ResponseEntity<?> getHomeDashBoard () {
        return ResponseEntity.ok().body(dashBoardService.getDashBoardHome());
    }
}
