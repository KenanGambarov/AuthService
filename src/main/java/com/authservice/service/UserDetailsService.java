package com.authservice.service;

import com.authservice.entity.UserEntity;
import com.authservice.security.UserPrincipal;

public interface UserDetailsService {

    UserEntity loadUserByUsername(String username);

    UserPrincipal getUserForPrincipal(String username);

    UserEntity saveUserDetails(UserEntity user);

    void clearUserCache(String username);

    boolean existsByUsername(String username);
}
