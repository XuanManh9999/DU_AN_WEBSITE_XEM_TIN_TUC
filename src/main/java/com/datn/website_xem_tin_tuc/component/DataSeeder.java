package com.datn.website_xem_tin_tuc.component;

import com.datn.website_xem_tin_tuc.config.security.AppConfig;
import com.datn.website_xem_tin_tuc.entity.RoleEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.entity.UserRoleEntity;
import com.datn.website_xem_tin_tuc.enums.Active;
import com.datn.website_xem_tin_tuc.enums.Role;
import com.datn.website_xem_tin_tuc.repository.RoleRepository;
import com.datn.website_xem_tin_tuc.repository.UserRepository;
import com.datn.website_xem_tin_tuc.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(AppConfig.class)
public class DataSeeder implements CommandLineRunner  {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args)  {
        seederRole();
        createAccountAdmin();
    }
    public void seederRole () {
        if (roleRepository.count() == 0) {
            var roleAdmin = new  RoleEntity();
            var roleUser = new RoleEntity();
            var roleManage= new RoleEntity();
            roleAdmin.setName(Role.ROLE_ADMIN);
            roleAdmin.setActive(Active.HOAT_DONG);
            roleUser.setName(Role.ROLE_USER);
            roleUser.setActive(Active.HOAT_DONG);
            roleManage.setName(Role.ROLE_MANAGE);
            roleManage.setActive(Active.HOAT_DONG);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
            roleRepository.save(roleManage);
        }
    }

    public void createAccountAdmin() {
        Optional<UserEntity> admin = userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            String passwordEncrypted = "$2a$10$NznSkz9.j8a2JAKa2aGYaOMD3wi6dB5Qt/6mwLmtB/Iqd6o38JlrC";
            user.setPassword(passwordEncrypted); // Mã hóa mật khẩu
            user.setAvatar("https://res.cloudinary.com/dpbo17rbt/image/upload/v1745083551/wvtsccbrg6oymhvgxv3k.png");
            user.setActive(Active.HOAT_DONG);
            userRepository.save(user);

            Optional<RoleEntity> roleEntity = roleRepository.findRoleEntityByName(Role.ROLE_ADMIN);
            if (roleEntity.isPresent()) {
                RoleEntity role = roleEntity.get();
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setRoleId(role);
                userRole.setUserId(user);
                userRoleRepository.save(userRole);
            }
        }
    }

}