package com.authservice.controller;

import com.authservice.dto.request.ChangePasswordRequest;
import com.authservice.dto.request.LoginRequest;
import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.AuthResponse;
import com.authservice.dto.response.TokenResponse;
import com.authservice.dto.response.UserResponse;
import com.authservice.mapper.UserMapper;
import com.authservice.security.UserPrincipal;
import com.authservice.service.AuthService;
import com.authservice.service.TokenService;
import com.authservice.service.UserDetailsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth/")
@AllArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody UserRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refreshToken(@RequestHeader("Authorization") String authHeader) {
        return tokenService.refreshToken(authHeader);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        authService.logout(user);
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request,
                               Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        authService.changePassword(user, request);
    }

    @GetMapping("/me")
    public UserResponse getProfile(Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return UserMapper.toResponse(userDetailsService.loadUserByUsername(user.getUsername()));
    }

}
