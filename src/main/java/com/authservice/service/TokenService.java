package com.authservice.service;


import com.authservice.dto.response.TokenResponse;
import com.authservice.entity.TokenEntity;

public interface TokenService {

    TokenResponse refreshToken(String authHeader);

    TokenEntity getUserValidToken(Long userId);

    void saveUserToken(TokenEntity tokenEntity);

    void clearUserValidTokenCache(Long userId);

}
