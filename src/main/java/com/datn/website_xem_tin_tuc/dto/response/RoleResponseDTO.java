package com.datn.website_xem_tin_tuc.dto.response;
import com.datn.website_xem_tin_tuc.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private Integer id;
    private Role name;
    private String descRole;
    private Date createdAt;
    private Date updatedAt;
}