package com.authservice.mapper;

import com.authservice.dto.response.AuthResponse;

public class AuthMapper {

    public static AuthResponse toAuthResponse(String jwtToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

}
