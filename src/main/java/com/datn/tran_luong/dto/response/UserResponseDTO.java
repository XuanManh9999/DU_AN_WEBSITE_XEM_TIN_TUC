package com.datn.tran_luong.dto.response;
import com.datn.tran_luong.enums.Active;
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
    private Long point;
    private String phoneNumber;
    private String gender;
    private String email;
    private Date birthday;
    private String background;
    private String avatar;
    private Active active;
    private Date createdAt;
    private Date updatedAt;
    private List<RoleResponseDTO> roles;
}