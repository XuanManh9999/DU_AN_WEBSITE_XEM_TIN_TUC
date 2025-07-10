package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.CommentRequest;
import com.datn.website_xem_tin_tuc.dto.request.UpdateCommentRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommentResponse;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.CommentEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.CommentRepository;
import com.datn.website_xem_tin_tuc.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticlesRepository articlesRepository;
    private final CurrentUser currentUser;

    @Override
    @Transactional
    public void createComment(CommentRequest request) {
        var user = currentUser.getCurrentUser();
        var article = articlesRepository.findById(request.getArticleId())
                .orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));

        CommentEntity parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Bình luận cha không tồn tại"));
        }

        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setArticles(article);
        comment.setParent(parent);
        comment.setContent(request.getContent());

        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByArticleSlug(String slug) {
        ArticlesEntity article = articlesRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với slug: " + slug));
        List<CommentEntity> roots = commentRepository.findByArticlesIdAndParentIsNullOrderByCreateAtDesc(article.getId());
        return roots.stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deleteComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);
    }

    @Override
    public CommonResponse updateComment(Long commentId, UpdateCommentRequest updateCommentRequest) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Không tìm thấy bình luận"));
        comment.setContent(updateCommentRequest.getContent());
        commentRepository.save(comment);
        return  CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật bài viết thành công")
                .build();
    }

    private CommentResponse mapToResponse(CommentEntity comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUsername(comment.getUser().getUsername());
        response.setCreatedAt(comment.getCreateAt());

        List<CommentResponse> replies = comment.getReplies().stream()
                .map(this::mapToResponse)
                .toList();
        response.setReplies(replies);
        return response;
    }
}
