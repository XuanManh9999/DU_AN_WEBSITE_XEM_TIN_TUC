
package com.datn.website_xem_tin_tuc.dto.cloudinary;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO cho kết quả upload file lên Cloudinary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryUploadResponseDTO {

    @JsonProperty("public_id")
    private String publicId;

    @JsonProperty("secure_url")
    private String secureUrl;

    @JsonProperty("url")
    private String url;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("original_filename")
    private String originalFilename;

    @JsonProperty("format")
    private String format;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("folder")
    private String folder;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("upload_success")
    private Boolean uploadSuccess;

    @JsonProperty("message")
    private String message;

    // Constructor để tạo response thành công
    public static CloudinaryUploadResponseDTO success(String publicId, String secureUrl, String originalFilename) {
        return CloudinaryUploadResponseDTO.builder()
                .publicId(publicId)
                .secureUrl(secureUrl)
                .originalFilename(originalFilename)
                .uploadSuccess(true)
                .message("Upload thành công")
                .build();
    }

    // Constructor để tạo response lỗi
    public static CloudinaryUploadResponseDTO error(String message) {
        return CloudinaryUploadResponseDTO.builder()
                .uploadSuccess(false)
                .message(message)
                .build();
    }
}