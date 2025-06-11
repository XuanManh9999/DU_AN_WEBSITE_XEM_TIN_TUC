package com.datn.website_xem_tin_tuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookmarkRequest {
    private Long userId;
    private Long articlesId;
}
