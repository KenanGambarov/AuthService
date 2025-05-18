package com.authservice.service;


import com.authservice.dto.response.TokenResponse;
import com.authservice.entity.TokenEntity;

import java.util.List;

public interface TokenService {

    TokenResponse refreshToken(String authHeader);

    List<TokenEntity> getAllValidToken(Long userId);

    void saveUserToken(TokenEntity tokenEntity);

    void saveAllToken(List<TokenEntity> token);

    void clearAllValidTokenCache(Long userId);

}
