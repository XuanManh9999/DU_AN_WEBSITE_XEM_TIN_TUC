package com.datn.website_xem_tin_tuc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticlesPerMonthDTO {
    private int month;
    private long count;
}
