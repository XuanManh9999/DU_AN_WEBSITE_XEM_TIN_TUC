package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.TagRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.TagResponse;
import com.datn.website_xem_tin_tuc.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<CommonResponse> create(@RequestBody TagRequest request) {
        TagResponse response = tagService.create(request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(200)
                .message("Create tag successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> update(@PathVariable Integer id, @RequestBody TagRequest request) {
        TagResponse response = tagService.update(id, request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(200)
                .message("Update tag successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Integer id) {
        tagService.delete(id);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(200)
                .message("Delete tag successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Integer id) {
        TagResponse response = tagService.getById(id);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(200)
                .message("Get tag successfully")
                .data(response)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAll() {
        List<TagResponse> tags = tagService.getAll();
        return ResponseEntity.ok(CommonResponse.builder()
                .status(200)
                .message("Get all tags successfully")
                .data(tags)
                .build());
    }
}
