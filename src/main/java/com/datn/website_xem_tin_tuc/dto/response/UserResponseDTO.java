package com.datn.website_xem_tin_tuc.dto.response;
import com.datn.website_xem_tin_tuc.enums.Active;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String background;
    private String avatar;
    private Active active;
    private Date createdAt;
    private Date updatedAt;
    private List<RoleResponseDTO> roles;
    private List<ArticlesResponseDTO> articles;
}