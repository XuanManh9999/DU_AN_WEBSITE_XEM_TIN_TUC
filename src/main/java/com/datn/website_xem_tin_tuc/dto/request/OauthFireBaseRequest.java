package com.datn.website_xem_tin_tuc.dto.request;

import lombok.Data;

@Data
public class OauthFireBaseRequest {
    private String id_token;
    private String email;
    private String provider;
    private String username;
    private String avatar;
}