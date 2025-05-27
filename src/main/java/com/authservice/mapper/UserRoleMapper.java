package com.authservice.mapper;

import com.authservice.dto.enums.RoleName;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.UserEntity;
import com.authservice.entity.UserRoleEntity;

public class UserRoleMapper {

    public static UserRoleEntity toEntity(UserEntity user,RoleEntity role){
        return UserRoleEntity.builder()
                .user(user)
                .role(role)
                .build();
    }

}
