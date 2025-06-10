package com.datn.website_xem_tin_tuc.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyOtpRequest {
    private String otp;
    private String email;
}