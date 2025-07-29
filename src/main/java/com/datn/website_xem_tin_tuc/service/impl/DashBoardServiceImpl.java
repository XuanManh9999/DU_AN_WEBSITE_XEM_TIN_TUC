package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.DashBoardHomeResponse;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.CategoryRepository;
import com.datn.website_xem_tin_tuc.repository.TagRepository;
import com.datn.website_xem_tin_tuc.repository.UserRepository;
import com.datn.website_xem_tin_tuc.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final UserRepository userRepository;
    private final ArticlesRepository articlesRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    @Override
    public CommonResponse getDashBoardHome() {
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin dashboard thành cônng")
                .data(DashBoardHomeResponse
                        .builder()
                        .quantityPosts(articlesRepository.count())
                        .quantityTags(tagRepository.count())
                        .quantityUsers(userRepository.count())
                        .quantityCategoryPosts(categoryRepository.count())
                        .build())
                .build();
    }
}
