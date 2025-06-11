package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.BookmarkRepository;
import com.datn.website_xem_tin_tuc.repository.CommentRepository;
import com.datn.website_xem_tin_tuc.repository.UserRepository;
import com.datn.website_xem_tin_tuc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticlesRepository articlesRepository;
    private final CurrentUser currentUser;
}
