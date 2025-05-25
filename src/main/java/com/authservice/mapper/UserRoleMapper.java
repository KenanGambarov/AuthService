package com.authservice.mapper;

import com.authservice.dto.enums.RoleName;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.UserRoleEntity;

public class UserRoleMapper {

    public static UserRoleEntity toEntity(RoleEntity roleEntity){
        return UserRoleEntity.builder()
                .role(roleEntity)
                .build();
    }

}
