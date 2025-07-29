package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ViewSyncScheduler {
    private final ArticlesService viewService;
    private final ArticlesRepository articlesRepository;
    // Chạy mỗi 5 phút
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void syncViewCounts() {
        Map<Long, Long> viewCounts = viewService.fetchAndResetViewCounts();

        for (Map.Entry<Long, Long> entry : viewCounts.entrySet()) {
            Long articleId = entry.getKey();
            Long count = entry.getValue();

            articlesRepository.findById(articleId).ifPresent(article -> {
                article.setView((int) (article.getView() + count));
                articlesRepository.save(article);
            });
        }
    }

}
