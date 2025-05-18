package com.authservice.service;

import com.authservice.dto.response.UserResponse;
import com.authservice.entity.UserEntity;

public interface UserDetailsService {

    UserResponse loadUserByUsername(String username);

    void saveUserDetails(UserEntity user);

    void clearUserCache(String username);

    boolean existsByUsername(String username);
}
