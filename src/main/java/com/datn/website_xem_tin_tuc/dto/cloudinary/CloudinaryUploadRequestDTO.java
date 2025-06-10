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
class CloudinaryUploadRequestDTO {

    @JsonProperty("folder")
    private String folder;

    @JsonProperty("public_id")
    private String publicId;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("crop")
    private String crop;

    @JsonProperty("quality")
    private String quality;

    @JsonProperty("format")
    private String format;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("overwrite")
    private Boolean overwrite;

    @JsonProperty("unique_filename")
    private Boolean uniqueFilename;
}
