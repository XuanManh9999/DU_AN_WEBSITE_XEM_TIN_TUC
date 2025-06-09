package com.datn.tran_luong.service.impl;

import com.datn.tran_luong.config.security.SecurityBeansConfig;
import com.datn.tran_luong.dto.request.UserRequest;
import com.datn.tran_luong.dto.response.CommonResponse;
import com.datn.tran_luong.dto.response.RoleResponseDTO;
import com.datn.tran_luong.dto.response.UserResponseDTO;
import com.datn.tran_luong.entity.RoleEntity;
import com.datn.tran_luong.entity.UserEntity;
import com.datn.tran_luong.entity.UserRoleEntity;
import com.datn.tran_luong.enums.Active;
import com.datn.tran_luong.exceptions.customs.DuplicateResourceException;
import com.datn.tran_luong.exceptions.customs.NotFoundException;
import com.datn.tran_luong.repository.RoleRepository;
import com.datn.tran_luong.repository.UserRepository;
import com.datn.tran_luong.repository.UserRoleRepository;
import com.datn.tran_luong.service.JwtService;
import com.datn.tran_luong.service.ManageUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageUserServiceImpl implements ManageUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final SecurityBeansConfig securityBeansConfig;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<CommonResponse> getAllUsers(Integer limit, Integer offset) {
        try {
            Pageable pageable = PageRequest.of(offset, limit);
            Page<UserEntity> userPage = userRepository.findAll(pageable);
            List<UserEntity> users = userPage.getContent();

            List<UserResponseDTO> userResponses = users.stream().map(user -> {
                UserResponseDTO userResponse = modelMapper.map(user, UserResponseDTO.class);
                userResponse.setCreatedAt(user.getCreateAt());
                userResponse.setUpdatedAt(user.getUpdateAt());

                List<RoleResponseDTO> roleResponseDTOS = new ArrayList<>();
                List<UserRoleEntity> userRoleEntities = user.getUserRoles();
                for (UserRoleEntity userRoleEntity : userRoleEntities) {
                    RoleEntity roleEntity = userRoleEntity.getRoleId();
                    RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
                    roleResponseDTO.setId(roleEntity.getId());
                    roleResponseDTO.setName(roleEntity.getName());
                    roleResponseDTO.setDescRole(roleEntity.getDescRole());
                    roleResponseDTO.setCreatedAt(roleEntity.getCreateAt());
                    roleResponseDTO.setUpdatedAt(roleEntity.getUpdateAt());
                    roleResponseDTOS.add(roleResponseDTO);
                }
                userResponse.setRoles(roleResponseDTOS);
                return userResponse;
            }).collect(Collectors.toList());

            CommonResponse response = CommonResponse.builder()
                    .status(HttpStatus.OK.value())
                    .totalPage(userPage.getTotalPages())
                    .data(userResponses)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public ResponseEntity<CommonResponse> getUserById(Long userId) {
        try {
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                UserResponseDTO userResponse = modelMapper.map(user.get(), UserResponseDTO.class);
                userResponse.setCreatedAt(user.get().getCreateAt());
                userResponse.setUpdatedAt(user.get().getUpdateAt());
                List<UserRoleEntity> userRoleEntitys = user.get().getUserRoles();
                List<RoleResponseDTO> RoleResponseDTOs = new ArrayList<>();
                for (UserRoleEntity userRoleEntity : userRoleEntitys) {
                    RoleEntity roleEntity = userRoleEntity.getRoleId();
                    RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
                    roleResponseDTO.setId(roleEntity.getId());
                    roleResponseDTO.setName(roleEntity.getName());
                    roleResponseDTO.setDescRole(roleEntity.getDescRole());
                    roleResponseDTO.setCreatedAt(roleEntity.getCreateAt());
                    roleResponseDTO.setUpdatedAt(roleEntity.getUpdateAt());
                    RoleResponseDTOs.add(roleResponseDTO);
                }

                userResponse.setRoles(RoleResponseDTOs);

                return ResponseEntity.ok().body(
                        CommonResponse.builder()
                                .status(HttpStatus.OK.value())
                                .data(userResponse)
                                .build()
                );

            }else {
                throw new UsernameNotFoundException("User not found");
            }
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponse> addUser(UserRequest userRequest) {
        try {
            Optional<UserEntity> user = userRepository.findByUsername(userRequest.getUser_name());

            if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
                Optional<UserEntity> user2 = userRepository.findByEmail(userRequest.getEmail());
                if (user2.isPresent()) {
                    throw new DuplicateResourceException("Tài khoản đã tồn tại trong hệ thống");
                }
            }

            if (user.isPresent() ) {
                throw new DuplicateResourceException("Tài khoản đã tồn tại trong hệ thống");
            }
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userRequest.getUser_name());

            if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
                userEntity.setEmail(userRequest.getEmail());
            }


            userEntity.setActive(Active.HOAT_DONG);
            userEntity.setPassword(securityBeansConfig.passwordEncoder().encode(userRequest.getPassword()));
            userEntity.setAvatar(userRequest.getAvatar());
            userRepository.save(userEntity);

            for (Integer role_id : userRequest.getRole_ids()) {
                Optional<RoleEntity> roleEntity = roleRepository.findById(role_id);
                if (roleEntity.isPresent()) {
                    UserRoleEntity userRoleEntity = new UserRoleEntity();
                    userRoleEntity.setRoleId(roleEntity.get());
                    userRoleEntity.setUserId(userEntity);
                    userRoleRepository.save(userRoleEntity);
                }else {
                    throw new NotFoundException("Role is not found");
                }
            }

            return ResponseEntity.ok().body(
                    CommonResponse
                            .builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Create User Done")
                            .build()
            );

        }catch (Exception e) {
            throw e;
        }

    }

    @Override
    public ResponseEntity<CommonResponse> updateUser(UserRequest userRequest, Long userId) {
        try {
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                UserEntity userEntity = user.get();
                userEntity.setUsername(userRequest.getUser_name());
                if (userRequest.getActive() != null) {
                    userEntity.setActive(userRequest.getActive());
                }
                if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                    userEntity.setPassword(securityBeansConfig.passwordEncoder().encode(userRequest.getPassword()));
                }
                userEntity.setAvatar(userRequest.getAvatar());
                userRepository.save(userEntity);
                userRoleRepository.deleteUserRolesByUserId(userId);

                for (Integer role_id : userRequest.getRole_ids()) {
                    Optional<RoleEntity> roleEntity = roleRepository.findById(role_id);

                    if (roleEntity.isPresent()) {
                        Optional<UserRoleEntity> userRoleEntity = userRoleRepository.findUserRoleEntitiesByUserIdAndRoleId(userEntity, roleEntity.get());
                        if (!userRoleEntity.isPresent()) {
                            UserRoleEntity userRoleEntity1 = new UserRoleEntity();
                            userRoleEntity1.setUserId(userEntity);
                            userRoleEntity1.setRoleId(roleEntity.get());
                            userRoleEntity1.setActive(Active.HOAT_DONG);
                            userRoleRepository.save(userRoleEntity1);
                        }
                    }else {
                        throw new NotFoundException("Role is not found");
                    }
                }
            }else {
                throw new NotFoundException("User is not found");
            }
            return ResponseEntity.ok().body(
                    CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Update User Done")
                            .build()
            );
        }catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<CommonResponse> deleteUser(Long userId) {
        try {
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                if (!user.get().isEnabled()) {
                    return ResponseEntity.ok().body(
                            CommonResponse
                                    .builder()
                                    .message("Tài khoản đang bị khóa")
                                    .status(HttpStatus.OK.value())
                                    .build()
                    );
                }
                UserEntity userEntity = user.get();
                userEntity.setActive(Active.CHUA_HOAT_DONG);
                userRepository.save(userEntity);

                UserResponseDTO userResponse = modelMapper.map(userEntity, UserResponseDTO.class);

                return ResponseEntity.ok().body(
                        CommonResponse
                                .builder()
                                .message("Delete User Done")
                                .status(HttpStatus.OK.value())
                                .data(userResponse)
                                .build()
                );
            }else {
                throw new  NotFoundException("User not found");
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getAllRoles() {
        try {
            List<RoleResponseDTO> roleResponseDTOS = new ArrayList<>();
            List<RoleEntity> roleEntities = roleRepository.findAll();
            for (RoleEntity roleEntity : roleEntities) {
                RoleResponseDTO roleResponseDTO = modelMapper.map(roleEntity, RoleResponseDTO.class);
                roleResponseDTO.setCreatedAt(roleEntity.getCreateAt());
                roleResponseDTO.setUpdatedAt(roleEntity.getUpdateAt());
                roleResponseDTOS.add(roleResponseDTO);
            }
            return ResponseEntity.ok().body(
                    CommonResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message("Get All Roles Done")
                            .data(roleResponseDTOS)
                            .build()
            );
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getUserByName(String name) {
        try {
            Optional<UserEntity> user = userRepository.findByUsername(name);
            if (user.isPresent()) {
                UserResponseDTO userResponse = modelMapper.map(user, UserResponseDTO.class);
                userResponse.setCreatedAt(user.get().getCreateAt());
                userResponse.setUpdatedAt(user.get().getUpdateAt());

                List<RoleResponseDTO> roleResponseDTOS = new ArrayList<>();
                List<UserRoleEntity> userRoleEntities = user.get().getUserRoles();
                for (UserRoleEntity userRoleEntity : userRoleEntities) {
                    RoleEntity roleEntity = userRoleEntity.getRoleId();
                    RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
                    roleResponseDTO.setId(roleEntity.getId());
                    roleResponseDTO.setName(roleEntity.getName());
                    roleResponseDTO.setDescRole(roleEntity.getDescRole());
                    roleResponseDTO.setCreatedAt(roleEntity.getCreateAt());
                    roleResponseDTO.setUpdatedAt(roleEntity.getUpdateAt());
                    roleResponseDTOS.add(roleResponseDTO);
                }
                userResponse.setRoles(roleResponseDTOS);
                return ResponseEntity.ok().body(
                        CommonResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Get User By Name Done")
                                .data(userResponse)
                                .build()
                );
            }else {
                throw new  NotFoundException("User not found");
            }
        }catch (Exception ex) {
            throw ex;
        }
    }

//    @Override
//    public ResponseEntity<CommonResponse> changeInfoUser(ChangeInfoUserRequest changeInfoUserRequest, String token) {
//        try {
//            String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
//            if (username != null) {
//                Optional<UserEntity> user = userRepository.findByUsername(username);
//                if (user.isPresent()) {
//                    UserEntity userEntity = user.get();
//                    userEntity.setPassword(securityBeansConfig.passwordEncoder().encode(changeInfoUserRequest.getPassword()));
//                    userEntity.setAvatar(changeInfoUserRequest.getAvatar());
//                    userEntity.setPhoneNumber(changeInfoUserRequest.getPhone_number());
//                    userRepository.save(userEntity);
//                    return ResponseEntity.ok().body(
//                            CommonResponse.builder()
//                                    .status(HttpStatus.OK.value())
//                                    .message("Update User Done")
//                                    .build()
//                    );
//                }else {
//                    throw new  NotFoundException("User not found");
//                }
//            }else {
//                throw new  NotFoundException("User not found");
//            }
//        }catch (Exception e) {
//            throw e;
//        }
//    }
}
