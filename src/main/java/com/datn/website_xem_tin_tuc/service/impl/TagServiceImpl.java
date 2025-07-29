package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.request.TagRequest;
import com.datn.website_xem_tin_tuc.dto.response.TagResponse;
import com.datn.website_xem_tin_tuc.entity.TagEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.TagRepository;
import com.datn.website_xem_tin_tuc.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagResponse create(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag already exists");
        }
        TagEntity tag = modelMapper.map(request, TagEntity.class);
        return modelMapper.map(tagRepository.save(tag), TagResponse.class);
    }

    @Override
    public TagResponse update(Integer id, TagRequest request) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        tag.setName(request.getName());
        tag.setDescription(request.getDescription());
        return modelMapper.map(tagRepository.save(tag), TagResponse.class);
    }

    @Override
    public void delete(Integer id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));

        if (!tag.getTagArticlesEntities().isEmpty()) {
            throw  new DuplicateResourceException("Không thể xóa Tag vì đã được sử dụng trong bài viết");
        }
        tagRepository.delete(tag);
    }

    @Override
    public TagResponse getById(Integer id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found"));
        return modelMapper.map(tag, TagResponse.class);
    }

    @Override
    public Page<TagResponse> getAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<TagEntity> tagEntities;
        if (keyword == null || keyword.trim().isEmpty()) {
            tagEntities = tagRepository.findAll(pageable);
        } else {
            tagEntities = tagRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return tagEntities.map(tag -> modelMapper.map(tag, TagResponse.class));
    }

}
