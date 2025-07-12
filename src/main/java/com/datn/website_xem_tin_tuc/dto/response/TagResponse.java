package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private Integer id;
    private String name;
    private String description;
    private Date createAt;
    private Date updateAt;
}
