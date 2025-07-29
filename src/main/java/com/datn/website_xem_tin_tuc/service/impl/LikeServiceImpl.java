package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.LikeRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.LikeResponseDTO;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.LikeEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.LikeRepository;
import com.datn.website_xem_tin_tuc.repository.UserRepository;
import com.datn.website_xem_tin_tuc.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ArticlesRepository articlesRepository;
    private final CurrentUser currentUser;

    @Override
    public CommonResponse getAllLikeByUser(int limit, int offset) {
        UserEntity user = currentUser.getCurrentUser();

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<LikeEntity> likePage = likeRepository.findAllByUser(user, pageable);

        List<LikeResponseDTO> likeResponseDTOS = likePage.stream()
                .map(like -> LikeResponseDTO.builder()
                        .id(like.getId())
                        .nameArticles(like.getArticles().getTitle())
                        .slug(like.getArticles().getSlug())
                        .content(like.getArticles().getContent())
                        .author(like.getUser().getUsername())
                        .view(like.getArticles().getView())
                        .userId(like.getUser().getId())
                        .thumbnail(like.getArticles().getThumbnail())
                        .slugCategory(like.getArticles().getCategory().getSlug())
                        .ArticlesId(like.getArticles().getId())
                        .categoryName(like.getArticles().getCategory().getName())
                        .createAt(like.getCreateAt())
                        .articlesCreateAt(like.getArticles().getCreateAt())
                        .build())
                .collect(Collectors.toList());

        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin chi tiết người dùng đã bookmark thành công")
                .data(likeResponseDTOS)
                .totalPages(likePage.getTotalPages())
                .currentPage(likePage.getNumber()) // = offset / limit
                .totalItems(likePage.getTotalElements())
                .build();
    }

    @Override
    public CommonResponse like(LikeRequest likeRequest) {
        UserEntity user = userRepository.findById(likeRequest.getUserId()).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        ArticlesEntity articles =  articlesRepository.findById(likeRequest.getArticlesId()).orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));
        if (likeRepository.existsByUserAndArticles(user, articles)) {
            throw new DuplicateResourceException("Người dùng đã like bài viết này rồi");
        }
        LikeEntity like = new LikeEntity();
        like.setArticles(articles);
        like.setUser(user);
        likeRepository.save(like);
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("like bài viết thành công")
                .build();
    }

    @Override
    public CommonResponse unlike(LikeRequest likeRequest) {
        UserEntity user = userRepository.findById(likeRequest.getUserId()).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
        ArticlesEntity articles =  articlesRepository.findById(likeRequest.getArticlesId()).orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));
        if (!likeRepository.existsByUserAndArticles(user, articles)) {
            throw new DuplicateResourceException("Người chưa đã like bài viết");
        }
        LikeEntity like = likeRepository.findByUserAndArticles(user, articles).orElseThrow(() -> new NotFoundException("Lịch sử like không tồn tại"));
        likeRepository.delete(like);
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Bỏ like bài viết thành công")
                .build();
    }
}
