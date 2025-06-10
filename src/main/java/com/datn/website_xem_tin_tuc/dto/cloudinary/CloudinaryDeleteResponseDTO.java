package com.datn.website_xem_tin_tuc.dto.cloudinary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryDeleteResponseDTO {

    @JsonProperty("public_id")
    private String publicId;

    @JsonProperty("result")
    private String result;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    public static CloudinaryDeleteResponseDTO success(String publicId) {
        return CloudinaryDeleteResponseDTO.builder()
                .publicId(publicId)
                .result("ok")
                .success(true)
                .message("Xóa file thành công")
                .build();
    }

    public static CloudinaryDeleteResponseDTO error(String publicId, String message) {
        return CloudinaryDeleteResponseDTO.builder()
                .publicId(publicId)
                .result("error")
                .success(false)
                .message(message)
                .build();
    }
}