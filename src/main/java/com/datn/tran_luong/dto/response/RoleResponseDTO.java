package com.datn.tran_luong.dto.response;
import com.datn.tran_luong.enums.Role;
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