package com.authservice.service;

import com.authservice.dto.response.UserResponse;
import com.authservice.entity.UserEntity;
import com.authservice.security.UserPrincipal;

public interface UserDetailsService {

    UserEntity loadUserByUsername(String username);

    UserPrincipal getUserForPrincipal(String username);

    void saveUserDetails(UserEntity user);

    void clearUserCache(String username);

    boolean existsByUsername(String username);
}
