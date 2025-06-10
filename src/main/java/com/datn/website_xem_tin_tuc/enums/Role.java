package com.datn.website_xem_tin_tuc.enums;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGE ("ROLE_MANAGE"),
    ROLE_USER ("ROLE_USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public static Role getById(String roleName) {
        for (Role role : values()) {
            if (role.roleName.equals(roleName)) {
                return role;
            }
        }
        return null; // Hoặc ném exception nếu không tìm thấy
    }
}