package com.datn.website_xem_tin_tuc.exceptions;

import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.exceptions.customs.ServerException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CommonResponse> duplicateResourceException(DuplicateResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
                CommonResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResponse> notFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                CommonResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build()
        );
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(
                CommonResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Token has expired, please login again.")
                        .build()
        );
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<CommonResponse> serverException(ServerException e) {
        e.printStackTrace(); // In thông tin chi tiết về lỗi ra console
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
                CommonResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build()
        );
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> exception(Exception e) {
        e.printStackTrace(); // In thông tin chi tiết về lỗi ra console
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
                CommonResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build()
        );
    }


}