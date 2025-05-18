package com.authservice.service;


import com.authservice.entity.TokenEntity;
import com.authservice.exception.ExceptionConstants;
import com.authservice.exception.UnAuthorizedException;

import java.util.List;
import java.util.Optional;

public interface TokenCacheService {

    Optional<List<TokenEntity>> getAllValidTokenFromCacheOrDB(Long id);

    Optional<TokenEntity> getTokenFromCacheOrDB(String token);

    void clearAllValidTokenCache(Long id);

    void clearTokenCache(String token);

}
