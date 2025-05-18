package com.authservice.service.impl;

import com.authservice.entity.UserEntity;
import com.authservice.repository.UserRepository;
import com.authservice.service.UserDetailsCacheService;
import com.authservice.util.CacheUtil;
import com.authservice.util.constraints.CacheConstraints;
import com.authservice.util.constraints.CacheDurationConstraints;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailsCacheServiceImpl implements UserDetailsCacheService {

    private final UserRepository userRepository;
    private final CacheUtil cacheUtil;

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackUserCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackUserCache")
    public Optional<UserEntity> getUserFromCacheOrDB(String username) {
        UserEntity user = cacheUtil.getOrLoad(CacheConstraints.USER_KEY.getKey(username),
                () -> userRepository.findByUsername(username).orElse(null),
                CacheDurationConstraints.DAY.toDuration());
        return Optional.ofNullable(user);
    }

    public Optional<UserEntity> fallbackUserCache(Long username, Throwable t) {
        log.error("Redis not available for username {}, falling back to DB. Error: {}",username, t.getMessage());
        return  Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackClearUserCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackClearUserCache")
    public void clearUserCache(String username) {
        cacheUtil.deleteFromCache(CacheConstraints.USER_KEY.getKey(username));
        log.debug("Cache cleared for username {}",  username);
    }

    public void fallbackClearUserCache(String username, Throwable t) {
        log.warn("Redis not available to clear cache for username {}, ignoring. Error: {}", username, t.getMessage());
    }
}
