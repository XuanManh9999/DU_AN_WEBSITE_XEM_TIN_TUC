package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.response.RoleResponseDTO;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> getAllRoles();
}
