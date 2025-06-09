package com.datn.tran_luong.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyOtpRequest {
    private String otp;
    private String email;
}