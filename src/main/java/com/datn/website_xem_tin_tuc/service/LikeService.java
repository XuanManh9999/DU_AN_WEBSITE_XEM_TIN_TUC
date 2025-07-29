package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.LikeRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

public interface LikeService {
    CommonResponse getAllLikeByUser(int limit,int offset);
    CommonResponse like(LikeRequest likeRequest);
    CommonResponse unlike (LikeRequest likeRequest);
}
