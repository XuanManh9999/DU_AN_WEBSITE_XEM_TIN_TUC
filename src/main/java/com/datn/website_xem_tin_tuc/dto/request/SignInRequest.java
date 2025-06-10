package com.datn.website_xem_tin_tuc.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInRequest implements Serializable {
    private String username;
    private String password;
    private String deviceToken;
    private String version;
}