package com.authservice.service.impl;

import com.authservice.dto.response.TokenResponse;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.TokenEntity;
import com.authservice.exception.ExceptionConstants;
import com.authservice.exception.UnAuthorizedException;
import com.authservice.mapper.TokenMapper;
import com.authservice.repository.TokenRepository;
import com.authservice.security.UserPrincipal;
import com.authservice.service.JwtService;
import com.authservice.service.TokenCacheService;
import com.authservice.service.TokenService;
import com.authservice.service.UserDetailsService;
import com.authservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final TokenCacheService tokenCacheService;

    @Override
    public TokenResponse refreshToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER)) {
            throw new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage());
        }

        String refreshToken = authHeader.substring(7);
        TokenEntity refreshTokenEntity = getTokenOrThrow(refreshToken);

        if (refreshTokenEntity.isRevoked() || refreshTokenEntity.isExpired()) {
            throw new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage());
        }

        String username = jwtService.extractUsername(refreshToken).orElseThrow(() ->
                new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage()));

        var userEntity = userDetailsService.loadUserByUsername(username);
        var roles = userEntity.getRoles().stream().map(RoleEntity::getName).toList();

        UserPrincipal user = new UserPrincipal(userEntity.getUsername(),userEntity.getPassword(),roles);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage());
        }

        refreshTokenEntity.setRevoked(true);
        refreshTokenEntity.setExpired(true);
        tokenRepository.save(refreshTokenEntity);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        tokenCacheService.clearTokenCache(refreshToken);
        return TokenMapper.toDto(newAccessToken,newRefreshToken);
    }

    @Override
    public TokenEntity getUserValidToken(Long userId) {
        return tokenCacheService.getUserValidTokenFromCacheOrDB(userId)
                .orElse(null);
    }

    @Override
    public void saveUserToken(TokenEntity tokenEntity) {
        tokenRepository.save(tokenEntity);

    }

    @Override
    public void clearUserValidTokenCache(Long userId) {
        tokenCacheService.clearUserValidTokenCache(userId);
    }

    private TokenEntity getTokenOrThrow(String refreshToken){
        return tokenCacheService.getTokenFromCacheOrDB(refreshToken)
                .orElseThrow(() -> new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage()));
    }
}
