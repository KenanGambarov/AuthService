package com.authservice.service.impl;

import com.authservice.dto.response.UserResponse;
import com.authservice.entity.UserEntity;
import com.authservice.exception.ExceptionConstants;
import com.authservice.exception.NotFoundException;
import com.authservice.mapper.UserMapper;
import com.authservice.repository.UserRepository;
import com.authservice.service.UserDetailsCacheService;
import com.authservice.service.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsCacheService cacheService;
    private final UserRepository userRepository;

    @Override
    public UserResponse loadUserByUsername(String username) {
        UserEntity user = cacheService.getUserFromCacheOrDB(username)
                .orElseThrow(() -> new NotFoundException(ExceptionConstants.USER_NOT_FOUND.getMessage()));
        return UserMapper.toResponse(user);
    }

    @Override
    public void saveUserDetails(UserEntity user) {
        userRepository.save(user);
    }


    @Override
    public void clearUserCache(String username) {
        cacheService.clearUserCache(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return  userRepository.existsByUsername(username);
    }



}
