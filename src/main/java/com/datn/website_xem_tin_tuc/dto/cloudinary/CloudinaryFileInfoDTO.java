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
public class CloudinaryFileInfoDTO {

    @JsonProperty("public_id")
    private String publicId;

    @JsonProperty("secure_url")
    private String secureUrl;

    @JsonProperty("url")
    private String url;

    @JsonProperty("format")
    private String format;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("type")
    private String type;

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

    @JsonProperty("updated_at")
    private String updatedAt;
}