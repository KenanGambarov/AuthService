package com.authservice.service;

import com.authservice.dto.request.ChangePasswordRequest;
import com.authservice.dto.request.LoginRequest;
import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.AuthResponse;
import com.authservice.dto.response.TokenResponse;
import com.authservice.security.UserPrincipal;

public interface AuthService {

    AuthResponse register(UserRequest request);

    TokenResponse login(LoginRequest request);

    void logout(UserPrincipal user);

    void changePassword(UserPrincipal user, ChangePasswordRequest request);

}
