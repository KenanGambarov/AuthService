package com.authservice.service.impl;

import com.authservice.entity.UserRoleEntity;
import com.authservice.repository.UserRoleRepository;
import com.authservice.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository roleRepository;

    @Override
    public void saveUserRoleDetails(UserRoleEntity userRoleEntity) {
        roleRepository.save(userRoleEntity);
    }
}
