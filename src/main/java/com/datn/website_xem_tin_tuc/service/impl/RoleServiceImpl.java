package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.response.RoleResponseDTO;
import com.datn.website_xem_tin_tuc.entity.RoleEntity;
import com.datn.website_xem_tin_tuc.repository.RoleRepository;
import com.datn.website_xem_tin_tuc.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        return roles.stream()
                .map(role ->  {
                    RoleResponseDTO roleResponseDTO =  modelMapper.map(role, RoleResponseDTO.class);
                    roleResponseDTO.setCreatedAt(role.getCreateAt());
                    roleResponseDTO.setUpdatedAt(role.getUpdateAt());
                    return roleResponseDTO;
                })
                .collect(Collectors.toList());
    }
}
