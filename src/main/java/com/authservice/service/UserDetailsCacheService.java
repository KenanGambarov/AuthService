package com.authservice.service;

import com.authservice.entity.UserEntity;

import java.util.Optional;

public interface UserDetailsCacheService {

    Optional<UserEntity> getUserFromCacheOrDB(String username);

    void clearUserCache(String username);


}
