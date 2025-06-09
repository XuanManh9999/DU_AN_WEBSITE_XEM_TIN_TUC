package com.datn.tran_luong.entity;

import com.datn.tran_luong.enums.Active;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "activeStatus", type = String.class))
@Filter(name = "activeFilter", condition = "active = :activeStatus")
public class UserEntity extends BaseEntity<Long> implements UserDetails, Serializable {

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;


    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "phoneNumber")
    private String phoneNumber;


    @Column(name = "background")
    private String background;

    @OneToMany(mappedBy = "userId", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserRoleEntity> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream()
                .map(userRole -> (GrantedAuthority) () -> userRole.getRoleId().getName().name())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Tài khoản chỉ được coi là "không bị khóa" nếu trạng thái active là HOAT_DONG.
     * Với các truy vấn thông thường, Hibernate Filter sẽ tự động lọc theo active = HOAT_DONG.
     * Còn với admin, ta có thể tắt filter để xem tất cả các tài khoản.
     */
    @Override
    public boolean isAccountNonLocked() {
        return super.getActive() == Active.HOAT_DONG;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Tài khoản chỉ được kích hoạt nếu active = HOAT_DONG.
     * Với admin, có thể tạm tắt Hibernate Filter để truy vấn tất cả.
     */
    @Override
    public boolean isEnabled() {
        return super.getActive() == Active.HOAT_DONG;
    }
}