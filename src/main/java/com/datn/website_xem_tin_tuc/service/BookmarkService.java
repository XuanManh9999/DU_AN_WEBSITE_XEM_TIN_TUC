package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.BookmarkRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

public interface BookmarkService {
    CommonResponse getAllBookMarkByUser(int limit, int offset);
    CommonResponse bookmark(BookmarkRequest bookmarkRequest);
    CommonResponse unBookmark(BookmarkRequest bookmarkRequest);
}
