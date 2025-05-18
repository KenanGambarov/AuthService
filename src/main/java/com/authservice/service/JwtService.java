package com.authservice.service;

import com.authservice.entity.UserEntity;
import com.authservice.security.UserPrincipal;

import java.util.Optional;

public interface JwtService {

    Optional<String> extractUsername(String token);

    String generateToken(UserPrincipal user);

    String generateRefreshToken(UserPrincipal user);

    boolean isTokenValid(String token, UserPrincipal user);
}
