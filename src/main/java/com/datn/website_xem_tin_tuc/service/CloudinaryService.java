package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.cloudinary.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CloudinaryService {
    /**
     * Upload file cơ bản lên Cloudinary
     * @param file MultipartFile cần upload
     * @return CloudinaryUploadResponseDTO chứa thông tin kết quả upload
     */
    CloudinaryUploadResponseDTO uploadFile(MultipartFile file);

    /**
     * Upload file vào folder cụ thể trên Cloudinary
     * @param file MultipartFile cần upload
     * @param folder Tên folder đích
     * @return CloudinaryUploadResponseDTO chứa thông tin kết quả upload
     */
    CloudinaryUploadResponseDTO uploadFileToFolder(MultipartFile file, String folder);

    /**
     * Upload file với các tùy chọn tùy chỉnh
     * @param file MultipartFile cần upload
     * @param options Map chứa các tùy chọn upload (width, height, quality, crop, etc.)
     * @return CloudinaryUploadResponseDTO chứa thông tin kết quả upload
     */
    CloudinaryUploadResponseDTO uploadFileWithOptions(MultipartFile file, Map<String, Object> options);

    /**
     * Upload nhiều file cùng lúc
     * @param files Mảng MultipartFile cần upload
     * @return List<CloudinaryUploadResponseDTO> chứa kết quả upload của từng file
     */
    List<CloudinaryUploadResponseDTO> uploadMultipleFiles(MultipartFile[] files);

    /**
     * Upload nhiều file vào folder cụ thể
     * @param files Mảng MultipartFile cần upload
     * @param folder Tên folder đích
     * @return List<CloudinaryUploadResponseDTO> chứa kết quả upload của từng file
     */
    List<CloudinaryUploadResponseDTO> uploadMultipleFilesToFolder(MultipartFile[] files, String folder);

    /**
     * Upload ảnh với resize tự động
     * @param file MultipartFile ảnh cần upload
     * @param width Chiều rộng mong muốn
     * @param height Chiều cao mong muốn
     * @return CloudinaryUploadResponseDTO chứa thông tin kết quả upload
     */
    CloudinaryUploadResponseDTO uploadImageWithResize(MultipartFile file, Integer width, Integer height);

    /**
     * Upload ảnh với resize và folder
     * @param file MultipartFile ảnh cần upload
     * @param width Chiều rộng mong muốn
     * @param height Chiều cao mong muốn
     * @param folder Tên folder đích
     * @return CloudinaryUploadResponseDTO chứa thông tin kết quả upload
     */
    CloudinaryUploadResponseDTO uploadImageWithResize(MultipartFile file, Integer width, Integer height, String folder);

    /**
     * Xóa file khỏi Cloudinary
     * @param publicId Public ID của file cần xóa
     * @return CloudinaryDeleteResponseDTO chứa thông tin kết quả xóa
     */
    CloudinaryDeleteResponseDTO deleteFile(String publicId);

    /**
     * Xóa nhiều file cùng lúc
     * @param publicIds List các Public ID cần xóa
     * @return List<CloudinaryDeleteResponseDTO> chứa kết quả xóa của từng file
     */
    List<CloudinaryDeleteResponseDTO> deleteMultipleFiles(List<String> publicIds);

    /**
     * Lấy thông tin chi tiết của file
     * @param publicId Public ID của file cần lấy thông tin
     * @return CloudinaryFileInfoDTO chứa thông tin file
     */
    CloudinaryFileInfoDTO getFileInfo(String publicId);

    /**
     * Kiểm tra file có hợp lệ không
     * @param file MultipartFile cần kiểm tra
     * @return true nếu file hợp lệ, false nếu không hợp lệ
     */
    boolean isValidFile(MultipartFile file);

    /**
     * Kiểm tra file có phải là ảnh không
     * @param file MultipartFile cần kiểm tra
     * @return true nếu là ảnh, false nếu không phải
     */
    boolean isImageFile(MultipartFile file);

    /**
     * Kiểm tra file có phải là video không
     * @param file MultipartFile cần kiểm tra
     * @return true nếu là video, false nếu không phải
     */
    boolean isVideoFile(MultipartFile file);

    /**
     * Lấy URL của file với transformation
     * @param publicId Public ID của file
     * @param transformations Map chứa các transformation (width, height, crop, quality, etc.)
     * @return URL đã được transform
     */
    String getTransformedUrl(String publicId, Map<String, Object> transformations);

    /**
     * Tạo URL thumbnail từ ảnh gốc
     * @param publicId Public ID của ảnh
     * @param width Chiều rộng thumbnail
     * @param height Chiều cao thumbnail
     * @return URL của thumbnail
     */
    String createThumbnailUrl(String publicId, Integer width, Integer height);
}
