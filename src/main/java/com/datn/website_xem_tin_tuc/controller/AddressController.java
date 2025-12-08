package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final RestTemplate restTemplate;
    private static final String API_BASE_URL = "https://provinces.open-api.vn/api";

    @GetMapping("/provinces")
    public ResponseEntity<CommonResponse> getProvinces() {
        try {
            List<?> provinces = restTemplate.getForObject(API_BASE_URL + "/p/", List.class);
            return ResponseEntity.ok(CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Lấy danh sách tỉnh/thành phố thành công")
                    .data(provinces)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lỗi khi lấy danh sách tỉnh/thành phố: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/districts")
    public ResponseEntity<CommonResponse> getDistricts(@RequestParam String provinceCode) {
        try {
            Map<String, Object> province = restTemplate.getForObject(
                    API_BASE_URL + "/p/" + provinceCode + "?depth=2", 
                    Map.class
            );
            if (province == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("Không tìm thấy tỉnh/thành phố")
                                .build());
            }
            List<?> districtList = (List<?>) province.get("districts");
            return ResponseEntity.ok(CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Lấy danh sách quận/huyện thành công")
                    .data(districtList)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lỗi khi lấy danh sách quận/huyện: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/wards")
    public ResponseEntity<CommonResponse> getWards(@RequestParam String districtCode) {
        try {
            Map<String, Object> district = restTemplate.getForObject(
                    API_BASE_URL + "/d/" + districtCode + "?depth=2", 
                    Map.class
            );
            if (district == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(CommonResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("Không tìm thấy quận/huyện")
                                .build());
            }
            List<?> wardList = (List<?>) district.get("wards");
            return ResponseEntity.ok(CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Lấy danh sách phường/xã thành công")
                    .data(wardList)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lỗi khi lấy danh sách phường/xã: " + e.getMessage())
                            .build());
        }
    }
}

