package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.BookmarkRequest;
import com.datn.website_xem_tin_tuc.dto.response.BookmarkResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.BookmarkEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.BookmarkRepository;
import com.datn.website_xem_tin_tuc.repository.UserRepository;
import com.datn.website_xem_tin_tuc.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ArticlesRepository articlesRepository;
    private final CurrentUser currentUser;

    @Override
    public CommonResponse getAllBookMarkByUser() {
        UserEntity user = currentUser.getCurrentUser();
        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findAllByUser(user);
        List<BookmarkResponseDTO> bookmarkResponseDTOS = new ArrayList<>();
         for(BookmarkEntity bookmark : bookmarkEntities) {
             bookmarkResponseDTOS.add(BookmarkResponseDTO.builder()
                             .id(bookmark.getId())
                             .nameArticles(bookmark.getArticles().getTitle())
                             .slug(bookmark.getArticles().getSlug())
                             .author(bookmark.getUser().getUsername())
                             .userId(bookmark.getUser().getId())
                             .ArticlesId(bookmark.getArticles().getId())
                             .categoryName(bookmark.getArticles().getCategory().getName())
                             .createAt(bookmark.getCreateAt())
                             .updateAt(bookmark.getUpdateAt())
                     .build());
        }
         return CommonResponse.builder()
                 .status(HttpStatus.OK.value())
                 .message("Lấy thông tin chi tiết người dùng đã bookmark thành công")
                 .data(bookmarkResponseDTOS)
                 .build();
    }

    @Override
    public CommonResponse bookmark(BookmarkRequest bookmarkRequest) {
        UserEntity user = userRepository.findById(bookmarkRequest.getUserId()).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        ArticlesEntity articles =  articlesRepository.findById(bookmarkRequest.getArticlesId()).orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));
        if (bookmarkRepository.existsByUserAndArticles(user, articles)) {
            throw new DuplicateResourceException("Người dùng đã bookmark bài viết này rồi");
        }
        BookmarkEntity bookmark = new BookmarkEntity();
        bookmark.setArticles(articles);
        bookmark.setUser(user);
        bookmarkRepository.save(bookmark);
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Bookmark bài viết thành công")
                .build();
    }

    @Override
    public CommonResponse unBookmark(BookmarkRequest bookmarkRequest) {
        UserEntity user = userRepository.findById(bookmarkRequest.getUserId()).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        ArticlesEntity articles =  articlesRepository.findById(bookmarkRequest.getArticlesId()).orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));
        if (!bookmarkRepository.existsByUserAndArticles(user, articles)) {
            throw new DuplicateResourceException("Người chưa đã bookmark bài viết");
        }
        BookmarkEntity bookmark = bookmarkRepository.findByUserAndArticles(user, articles).orElseThrow(() -> new NotFoundException("Lịch sử bookmark không tồn tại"));
        bookmarkRepository.delete(bookmark);
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Bỏ bookmark bài viết thành công")
                .build();
    }
}
