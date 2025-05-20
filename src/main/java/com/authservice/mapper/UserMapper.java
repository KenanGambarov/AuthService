package com.authservice.mapper;

import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.UserResponse;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.UserEntity;
import com.authservice.security.UserPrincipal;

import java.util.Set;

public class UserMapper {

    public static UserEntity toEntity(UserRequest request, String password, RoleEntity role) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(password)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .roles(Set.of(role))
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
        var roles = userEntity.getRoles().stream().map(RoleEntity::getName).toList();
        return new UserPrincipal(userEntity.getUsername(),userEntity.getPassword(),roles);
    }


}
