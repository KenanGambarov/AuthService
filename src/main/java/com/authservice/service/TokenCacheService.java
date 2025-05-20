package com.authservice.service;


import com.authservice.entity.TokenEntity;
import com.authservice.exception.ExceptionConstants;
import com.authservice.exception.UnAuthorizedException;

import java.util.List;
import java.util.Optional;

public interface TokenCacheService {

    Optional<TokenEntity> getUserValidTokenFromCacheOrDB(Long id);

    Optional<TokenEntity> getTokenFromCacheOrDB(String token);

    void clearUserValidTokenCache(Long id);

    void clearTokenCache(String token);

}
