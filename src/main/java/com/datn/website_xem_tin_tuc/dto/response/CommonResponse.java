package com.datn.website_xem_tin_tuc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse implements Serializable {
    private int status;
    private String message;
    private Object data;
    private Integer totalPages;
    private Integer totalPage;
    private Integer currentPage;
    private Long totalItems;
}