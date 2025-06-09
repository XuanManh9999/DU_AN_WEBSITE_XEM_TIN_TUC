package com.datn.tran_luong.dto.request;

import com.datn.tran_luong.enums.Active;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserRequest {
    private String user_name;
    private Long point;
    private String phone_number;
    private String password;
    private Active active;
    private String email;
    private Date birthday;
    private String avatar;
    private String backgroud;
    private List<Integer> role_ids;
}