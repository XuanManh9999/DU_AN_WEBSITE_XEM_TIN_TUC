package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.BookmarkRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
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
    public ResponseEntity<CommonResponse> getBookmarks(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(bookmarkService.getAllBookMarkByUser(limit, offset));
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
