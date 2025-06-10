package com.datn.website_xem_tin_tuc.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.datn.website_xem_tin_tuc.dto.cloudinary.CloudinaryDeleteResponseDTO;
import com.datn.website_xem_tin_tuc.dto.cloudinary.CloudinaryFileInfoDTO;
import com.datn.website_xem_tin_tuc.dto.cloudinary.CloudinaryUploadResponseDTO;
import com.datn.website_xem_tin_tuc.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    // Danh sách các định dạng ảnh được hỗ trợ
    private static final Set<String> IMAGE_FORMATS = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif",
            "image/bmp", "image/webp", "image/tiff", "image/svg+xml"
    );

    // Danh sách các định dạng video được hỗ trợ
    private static final Set<String> VIDEO_FORMATS = Set.of(
            "video/mp4", "video/avi", "video/mov", "video/wmv",
            "video/flv", "video/webm", "video/mkv"
    );

    @Override
    public CloudinaryUploadResponseDTO uploadFile(MultipartFile file) {
        log.info("Bắt đầu upload file: {}", file.getOriginalFilename());

        try {
            if (!isValidFile(file)) {
                log.warn("File không hợp lệ: {}", file.getOriginalFilename());
                return CloudinaryUploadResponseDTO.error("File không hợp lệ");
            }

            String publicId = generatePublicId(file.getOriginalFilename());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "auto"
                    )
            );

            log.info("Upload file thành công: {}", file.getOriginalFilename());
            return mapToUploadResponseDTO(uploadResult, file.getOriginalFilename());

        } catch (IOException e) {
            log.error("Lỗi khi upload file: {}", e.getMessage(), e);
            return CloudinaryUploadResponseDTO.error("Lỗi khi upload file: " + e.getMessage());
        }
    }

    @Override
    public CloudinaryUploadResponseDTO uploadFileToFolder(MultipartFile file, String folder) {
        log.info("Bắt đầu upload file {} vào folder: {}", file.getOriginalFilename(), folder);

        try {
            if (!isValidFile(file)) {
                return CloudinaryUploadResponseDTO.error("File không hợp lệ");
            }

            String publicId = folder + "/" + generatePublicId(file.getOriginalFilename());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "auto",
                            "folder", folder
                    )
            );

            log.info("Upload file vào folder thành công: {} -> {}", file.getOriginalFilename(), folder);
            return mapToUploadResponseDTO(uploadResult, file.getOriginalFilename());

        } catch (IOException e) {
            log.error("Lỗi khi upload file vào folder: {}", e.getMessage(), e);
            return CloudinaryUploadResponseDTO.error("Lỗi khi upload file: " + e.getMessage());
        }
    }

    @Override
    public CloudinaryUploadResponseDTO uploadFileWithOptions(MultipartFile file, Map<String, Object> options) {
        log.info("Bắt đầu upload file với options: {}", file.getOriginalFilename());

        try {
            if (!isValidFile(file)) {
                return CloudinaryUploadResponseDTO.error("File không hợp lệ");
            }

            // Tạo bản sao của options để tránh modify original map
            Map<String, Object> uploadOptions = new HashMap<>(options);

            if (!uploadOptions.containsKey("public_id")) {
                uploadOptions.put("public_id", generatePublicId(file.getOriginalFilename()));
            }

            if (!uploadOptions.containsKey("resource_type")) {
                uploadOptions.put("resource_type", "auto");
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadOptions
            );

            log.info("Upload file với options thành công: {}", file.getOriginalFilename());
            return mapToUploadResponseDTO(uploadResult, file.getOriginalFilename());

        } catch (IOException e) {
            log.error("Lỗi khi upload file với options: {}", e.getMessage(), e);
            return CloudinaryUploadResponseDTO.error("Lỗi khi upload file: " + e.getMessage());
        }
    }

    @Override
    public List<CloudinaryUploadResponseDTO> uploadMultipleFiles(MultipartFile[] files) {
        log.info("Bắt đầu upload {} files", files.length);

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @Override
    public List<CloudinaryUploadResponseDTO> uploadMultipleFilesToFolder(MultipartFile[] files, String folder) {
        log.info("Bắt đầu upload {} files vào folder: {}", files.length, folder);

        return Arrays.stream(files)
                .map(file -> uploadFileToFolder(file, folder))
                .collect(Collectors.toList());
    }

    @Override
    public CloudinaryUploadResponseDTO uploadImageWithResize(MultipartFile file, Integer width, Integer height) {
        Map<String, Object> options = ObjectUtils.asMap(
                "width", width,
                "height", height,
                "crop", "fill",
                "quality", "auto",
                "fetch_format", "auto"
        );

        return uploadFileWithOptions(file, options);
    }

    @Override
    public CloudinaryUploadResponseDTO uploadImageWithResize(MultipartFile file, Integer width, Integer height, String folder) {
        Map<String, Object> options = ObjectUtils.asMap(
                "width", width,
                "height", height,
                "crop", "fill",
                "quality", "auto",
                "fetch_format", "auto",
                "folder", folder
        );

        return uploadFileWithOptions(file, options);
    }

    @Override
    public CloudinaryDeleteResponseDTO deleteFile(String publicId) {
        log.info("Bắt đầu xóa file: {}", publicId);

        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            if ("ok".equals(result.get("result"))) {
                log.info("Xóa file thành công: {}", publicId);
                return CloudinaryDeleteResponseDTO.success(publicId);
            } else {
                log.warn("Không thể xóa file: {}", publicId);
                return CloudinaryDeleteResponseDTO.error(publicId, "Không thể xóa file");
            }

        } catch (IOException e) {
            log.error("Lỗi khi xóa file {}: {}", publicId, e.getMessage(), e);
            return CloudinaryDeleteResponseDTO.error(publicId, "Lỗi khi xóa file: " + e.getMessage());
        }
    }

    @Override
    public List<CloudinaryDeleteResponseDTO> deleteMultipleFiles(List<String> publicIds) {
        log.info("Bắt đầu xóa {} files", publicIds.size());

        return publicIds.stream()
                .map(this::deleteFile)
                .collect(Collectors.toList());
    }

    @Override
    public CloudinaryFileInfoDTO getFileInfo(String publicId) {
        log.info("Lấy thông tin file: {}", publicId);

        try {
            Map<String, Object> result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return mapToFileInfoDTO(result);

        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin file {}: {}", publicId, e.getMessage(), e);
            return CloudinaryFileInfoDTO.builder()
                    .publicId(publicId)
                    .build();
        }
    }

    @Override
    public boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getSize() > 0;
    }

    @Override
    public boolean isImageFile(MultipartFile file) {
        if (!isValidFile(file)) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && IMAGE_FORMATS.contains(contentType.toLowerCase());
    }

    @Override
    public boolean isVideoFile(MultipartFile file) {
        if (!isValidFile(file)) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && VIDEO_FORMATS.contains(contentType.toLowerCase());
    }

    @Override
    public String getTransformedUrl(String publicId, Map<String, Object> transformations) {
        try {
            return cloudinary.url()
                    .publicId(publicId)
                    .transformation(mapToTransformation(transformations))
                    .generate();
        } catch (Exception e) {
            log.error("Lỗi khi tạo transformed URL cho {}: {}", publicId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String createThumbnailUrl(String publicId, Integer width, Integer height) {
        Map<String, Object> transformations = ObjectUtils.asMap(
                "width", width,
                "height", height,
                "crop", "fill",
                "quality", "auto",
                "fetch_format", "auto"
        );

        return getTransformedUrl(publicId, transformations);
    }

    /**
     * Tạo public_id duy nhất cho file
     */
    private String generatePublicId(String originalFilename) {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        if (originalFilename != null && originalFilename.contains(".")) {
            String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            // Làm sạch tên file, chỉ giữ lại ký tự an toàn
            String cleanName = nameWithoutExt.replaceAll("[^a-zA-Z0-9_-]", "_");
            return cleanName + "_" + uuid;
        }

        return "file_" + uuid;
    }

    /**
     * Map kết quả upload thành DTO
     */
    private CloudinaryUploadResponseDTO mapToUploadResponseDTO(Map<String, Object> uploadResult, String originalFilename) {
        return CloudinaryUploadResponseDTO.builder()
                .publicId(getString(uploadResult, "public_id"))
                .secureUrl(getString(uploadResult, "secure_url"))
                .url(getString(uploadResult, "url"))
                .displayName(getString(uploadResult, "display_name"))
                .originalFilename(originalFilename)
                .format(getString(uploadResult, "format"))
                .resourceType(getString(uploadResult, "resource_type"))
                .fileSize(getLong(uploadResult, "bytes"))
                .width(getInteger(uploadResult, "width"))
                .height(getInteger(uploadResult, "height"))
                .folder(getString(uploadResult, "folder"))
                .createdAt(getString(uploadResult, "created_at"))
                .uploadSuccess(true)
                .message("Upload thành công")
                .build();
    }

    /**
     * Map thông tin file thành DTO
     */
    private CloudinaryFileInfoDTO mapToFileInfoDTO(Map<String, Object> fileInfo) {
        return CloudinaryFileInfoDTO.builder()
                .publicId(getString(fileInfo, "public_id"))
                .secureUrl(getString(fileInfo, "secure_url"))
                .url(getString(fileInfo, "url"))
                .format(getString(fileInfo, "format"))
                .resourceType(getString(fileInfo, "resource_type"))
                .type(getString(fileInfo, "type"))
                .fileSize(getLong(fileInfo, "bytes"))
                .width(getInteger(fileInfo, "width"))
                .height(getInteger(fileInfo, "height"))
                .folder(getString(fileInfo, "folder"))
                .createdAt(getString(fileInfo, "created_at"))
                .updatedAt(getString(fileInfo, "updated_at"))
                .build();
    }

    // Helper methods để safely extract values từ Map
    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer getInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Long getLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Transformation mapToTransformation(Map<String, Object> map) {
        Transformation t = new Transformation();
        if (map.containsKey("width")) t.width((Integer) map.get("width"));
        if (map.containsKey("height")) t.height((Integer) map.get("height"));
        if (map.containsKey("crop")) t.crop((String) map.get("crop"));
        if (map.containsKey("quality")) t.quality((String) map.get("quality"));
        if (map.containsKey("fetch_format")) t.fetchFormat((String) map.get("fetch_format"));
        return t;
    }
}
