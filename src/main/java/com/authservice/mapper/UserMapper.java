package com.authservice.mapper;

import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.UserResponse;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.UserEntity;

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
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }

    public static UserEntity toEntity(UserResponse response) {
        return UserEntity.builder()
                .username(response.getUsername())
                .password(response.getPassword())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .phone(response.getPhone())
                .build();
    }

}
