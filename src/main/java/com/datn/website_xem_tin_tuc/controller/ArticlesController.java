package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.ArticlesRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.enums.TypeArticles;
import com.datn.website_xem_tin_tuc.service.ArticlesService;
import com.google.api.Http;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticlesService articlesService;
    private final CurrentUser currentUser;
//    Dùng redis
//    @PostMapping("/{id}/view")
//    public ResponseEntity<?> increaseView(@PathVariable Long id, HttpServletRequest request) {
//        String userKey;
//
//        // Nếu có login thì lấy userId, không thì lấy IP
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
//            userKey = currentUser.getCurrentUser().getId().toString();
//        } else {
//            userKey = request.getRemoteAddr(); // IP Address fallback
//        }
//
//        articlesService.increaseView(id, userKey);
//        return ResponseEntity.ok().build();
//    }

    // Không dùng redis

    @PostMapping("/{id}/view")
    public ResponseEntity<?> increaseView(@PathVariable Long id, HttpServletRequest request) {
        articlesService.increaseView(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllArticle(@RequestParam(name = "limit", defaultValue = "10") int limit,
                                           @RequestParam(name = "offset", defaultValue = "0") int offset,
                                           @RequestParam(name = "sortBy", defaultValue = "view") String sortBy,
                                           @RequestParam(name = "order", defaultValue = "desc") String order,
                                           @RequestParam(name = "title", required = false) String title) {
        return ResponseEntity.status(HttpStatus.OK).body(articlesService.getAllArticles(limit, offset, sortBy, order, title));
    }

    @GetMapping("/{articlesId}")
    public ResponseEntity<?> getArticleById(@PathVariable(name = "articlesId") Long articlesId) {
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy bài viết theo id thành công")
                .data(articlesService.getArticleById(articlesId))
                .build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getArticleBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy bài viết theo slug thành công")
                .data(articlesService.getArticleBySlug(slug))
                .build());
    }


    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<?> getArticleByCategoryId (@PathVariable  Long categoryId) {
        return ResponseEntity.ok(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy bài viết theo chủ đề thành công")
                        .data(articlesService.getArticleByCategoryId(categoryId))
                .build());
    }

    @PostMapping("")
    public ResponseEntity<?> createArticle(@RequestParam(required = false) String title,
                                           @RequestParam(required = false) String slug,
                                           @RequestParam(required = false) String content,
                                           @RequestParam(required = false) MultipartFile thumbnail,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) String type,
                                           @RequestParam(required = false) List<Long> tagIds
    ) throws BadRequestException {
        TypeArticles typeEnum = null;
        if (type != null) {
            try {
                typeEnum = TypeArticles.valueOf(type.toUpperCase()); // Convert String -> Enum
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Loại bài viết không hợp lệ: " + type);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(CommonResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Thêm bài viết thành công")
                        .data(articlesService.createArticle(title, slug, content, thumbnail, typeEnum, tagIds, categoryId))
                        .build());
    }

    @PutMapping("/{articlesId}")
    public ResponseEntity<?> updateArticle(
            @PathVariable Long articlesId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile thumbnail,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String type
    ) throws BadRequestException {

        TypeArticles typeEnum = null;
        if (type != null) {
            try {
                typeEnum = TypeArticles.valueOf(type.toUpperCase()); // Convert String -> Enum
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Loại bài viết không hợp lệ: " + type);
            }
        }
        ArticlesRequest articlesRequest = new ArticlesRequest(title, slug, content, typeEnum, authorId, categoryId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật bài viết thành công")
                        .data(articlesService.updateArticle(articlesId, articlesRequest, thumbnail))
                        .build());
    }

    @DeleteMapping("/{articlesId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long articlesId) {
        articlesService.deleteArticle(articlesId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa bài viết thành công")
                        .build());
    }
}
