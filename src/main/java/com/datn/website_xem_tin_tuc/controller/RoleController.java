package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.response.RoleResponseDTO;
import com.datn.website_xem_tin_tuc.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public List<RoleResponseDTO> getAllRoles() {
        return roleService.getAllRoles();
    }
}
