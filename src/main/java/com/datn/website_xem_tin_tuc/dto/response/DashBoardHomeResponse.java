package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardHomeResponse {
    private Long quantityPosts;
    private Long quantityCategoryPosts;
    private Long quantityTags;
    private Long quantityUsers;
}
