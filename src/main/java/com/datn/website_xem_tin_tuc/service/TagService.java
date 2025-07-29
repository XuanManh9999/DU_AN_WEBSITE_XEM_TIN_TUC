package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.TagRequest;
import com.datn.website_xem_tin_tuc.dto.response.TagResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagService {
    TagResponse create(TagRequest request);
    TagResponse update(Integer id, TagRequest request);
    void delete(Integer id);
    TagResponse getById(Integer id);
    Page<TagResponse> getAll(String keyword, int page, int size);
}
