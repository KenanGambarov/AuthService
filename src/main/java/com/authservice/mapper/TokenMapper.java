package com.authservice.mapper;

import com.authservice.dto.response.TokenResponse;
import com.authservice.entity.TokenEntity;
import com.authservice.entity.UserEntity;
import com.authservice.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;

public class TokenMapper {

    public static TokenEntity toEntity(UserEntity user, String jwtToken, boolean isExpired, boolean isRevoked) {
        return TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .expired(isExpired)
                .revoked(isRevoked)
                .build();
    }

    public static String toJwt(UserPrincipal user, Date isuedDate, Date expDdate, Key signKey, SignatureAlgorithm algorithm) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(isuedDate)
                .setExpiration(expDdate)
                .signWith(signKey, algorithm)
                .compact();
    }

    public static TokenResponse toDto(String newAccessToken,String newRefreshToken){
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


}
