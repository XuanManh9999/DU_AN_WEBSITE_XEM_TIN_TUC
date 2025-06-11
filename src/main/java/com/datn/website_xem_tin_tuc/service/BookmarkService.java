package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.BookmarkRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

public interface BookmarkService {
    CommonResponse getAllBookMarkByUser();
    CommonResponse bookmark(BookmarkRequest bookmarkRequest);
    CommonResponse unBookmark(BookmarkRequest bookmarkRequest);
}
