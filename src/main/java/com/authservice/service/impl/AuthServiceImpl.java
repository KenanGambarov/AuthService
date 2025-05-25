package com.authservice.service.impl;

import com.authservice.dto.enums.RoleName;
import com.authservice.dto.request.ChangePasswordRequest;
import com.authservice.dto.request.LoginRequest;
import com.authservice.dto.request.UserRequest;
import com.authservice.dto.response.AuthResponse;
import com.authservice.dto.response.TokenResponse;
import com.authservice.entity.RoleEntity;
import com.authservice.entity.TokenEntity;
import com.authservice.entity.UserEntity;
import com.authservice.exception.*;
import com.authservice.mapper.AuthMapper;
import com.authservice.mapper.TokenMapper;
import com.authservice.mapper.UserMapper;
import com.authservice.repository.RoleRepository;
import com.authservice.security.UserPrincipal;
import com.authservice.service.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(UserRequest request) {
        if (userDetailsService.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException(ExceptionConstants.ALREADY_EXIST.
                    getMessagePattern(StringUtils.capitalize(UserRequest.Fields.username)));
        }

        var role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new NotFoundException(ExceptionConstants.DEFAULT_ROLE_NOT_FOUND
                        .getMessagePattern(RoleName.USER)));

        var user =  UserMapper.toEntity(request,passwordEncoder.encode(request.getPassword()),role);
        userDetailsService.saveUserDetails(user);

        var jwtToken = jwtService.generateToken(new UserPrincipal(request.getUsername(),request.getPassword(), List.of(role.getName())));
        var refreshToken = jwtService.generateRefreshToken(new UserPrincipal(request.getUsername(),request.getPassword(),List.of(role.getName())));

        saveUserToken(user, refreshToken);

        return AuthMapper.toAuthResponse(jwtToken,refreshToken);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserEntity userEntity = userDetailsService.loadUserByUsername(request.getUsername());
//            System.out.println("userEntity " + userEntity);
            var roles = userEntity.getRoles().stream().map(RoleEntity::getName).toList();
            System.out.println("roles " + userEntity.getRoles());
            UserPrincipal userPrincipal = new UserPrincipal(userEntity.getUsername(),userEntity.getPassword(),roles);

            String accessToken = jwtService.generateToken(userPrincipal);
            String refreshToken = jwtService.generateRefreshToken(userPrincipal);

            invalidateUserToken(userEntity.getId(), userEntity.getUsername());
            saveUserToken(userEntity,refreshToken);
            return TokenMapper.toDto(accessToken,refreshToken);

        } catch (BadCredentialsException ex) {
            throw new UnAuthorizedException(ExceptionConstants.BAD_CREDENTIALS.getMessage());
        }
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        tokenService.saveUserToken(TokenMapper.toEntity(user,jwtToken,false,false));
    }

    @Override
    @Transactional
    public void logout(UserPrincipal user) {
        UserEntity userEntity = userDetailsService.loadUserByUsername(user.getUsername());
        invalidateUserToken(userEntity.getId(), user.getUsername());
    }

    @Override
    @Transactional
    public void changePassword(UserPrincipal user, ChangePasswordRequest request) {
        UserEntity userEntity = userDetailsService.loadUserByUsername(user.getUsername());

        if (!passwordEncoder.matches(request.getOldPassword(), userEntity.getPassword())) {
            throw new BadRequestException(ExceptionConstants.WRONG_PASSWORD.getMessage());
        }

        userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userDetailsService.saveUserDetails(userEntity);
        invalidateUserToken(userEntity.getId(), user.getUsername());
    }

    private void invalidateUserToken(Long userId, String username){
        TokenEntity token =  tokenService.getUserValidToken(userId);
        if (token!=null) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenService.saveUserToken(token);
            userDetailsService.clearUserCache(username);
            tokenService.clearUserValidTokenCache(userId);
        }
    }

}
