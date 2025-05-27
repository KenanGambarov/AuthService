package com.authservice.mapper;

import com.authservice.dto.enums.RoleName;
import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.UserResponse;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.UserEntity;
import com.authservice.entity.UserRoleEntity;
import com.authservice.security.UserPrincipal;

import java.util.List;

public class UserMapper {

    public static UserEntity toEntity(UserRequest request, String password) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(password)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    public static UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .username(entity.getUsername())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }

    public static UserPrincipal toUserPrincipal(UserEntity userEntity) {
        return new UserPrincipal(userEntity.getId(),userEntity.getUsername(),userEntity.getPassword(),entityToRoleList(userEntity));
    }

    public static List<RoleName> entityToRoleList(UserEntity userEntity){
        return userEntity.getUserRoles().stream().map(UserRoleEntity::getRole).toList()
                .stream().map(RoleEntity::getName).toList();
    }


}
