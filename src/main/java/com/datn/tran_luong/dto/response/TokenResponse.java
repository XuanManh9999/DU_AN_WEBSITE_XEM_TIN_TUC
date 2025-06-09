package com.datn.tran_luong.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Integer status;
}
