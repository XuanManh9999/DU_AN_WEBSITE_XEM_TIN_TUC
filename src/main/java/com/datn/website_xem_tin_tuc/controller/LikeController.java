package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.LikeRequest;
import com.datn.website_xem_tin_tuc.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @GetMapping("/all-like-by-user")
    public ResponseEntity<?> getAllLikeByUser() {
        return ResponseEntity.status(HttpStatus.OK.value()).body(likeService.getAllLikeByUser());
    }

    @PostMapping("")
    public ResponseEntity<?> like (@RequestBody LikeRequest likeRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(likeService.like(likeRequest));
    }

    @DeleteMapping("")
    public ResponseEntity<?> unlike (@RequestBody LikeRequest likeRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(likeService.unlike(likeRequest));
    }
}
