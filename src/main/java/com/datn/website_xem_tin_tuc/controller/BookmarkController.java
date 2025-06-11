package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.BookmarkRequest;
import com.datn.website_xem_tin_tuc.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/all-by-user")
    public ResponseEntity<?> getAllBookmarkByUser() {
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(bookmarkService.getAllBookMarkByUser());
    }

    @PostMapping("")
    public ResponseEntity<?> bookmark (@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(bookmarkService.bookmark(bookmarkRequest));
    }

    @DeleteMapping("")
    public ResponseEntity<?> unbookmark (@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(bookmarkService.unBookmark(bookmarkRequest));
    }
}
