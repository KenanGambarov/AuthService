package com.authservice.service.impl;

import com.authservice.entity.TokenEntity;
import com.authservice.repository.TokenRepository;
import com.authservice.service.TokenCacheService;
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
public class TokenCacheServiceImpl implements TokenCacheService {

    private final TokenRepository tokenRepository;
    private final CacheUtil cacheUtil;

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackAllValidTokenCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackAllValidTokenCache")
    public Optional<TokenEntity> getUserValidTokenFromCacheOrDB(Long userId) {
        TokenEntity tokens = cacheUtil.getOrLoad(CacheConstraints.ALL_TOKEN_KEY.getKey(userId),
                () -> tokenRepository.findValidTokenByUser(userId).orElse(null),
                CacheDurationConstraints.WEEK.toDuration());
        return Optional.ofNullable(tokens);
    }

    public Optional<TokenEntity> fallbackAllValidTokenCache(Long userId, Throwable t) {
        log.error("Redis not available for userId {}, falling back to DB. Error: {}",userId, t.getMessage());
        return  Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackTokenCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackTokenCache")
    public Optional<TokenEntity> getTokenFromCacheOrDB(String token) {
        TokenEntity tokenEntity = cacheUtil.getOrLoad(CacheConstraints.TOKEN_KEY.getKey(token),
                () -> tokenRepository.findByToken(token).orElse(null),
                CacheDurationConstraints.WEEK.toDuration());
        return Optional.ofNullable(tokenEntity);
    }

    public Optional<TokenEntity> fallbackTokenCache(String token, Throwable t) {
        log.error("Redis not available for token {}, falling back to DB. Error: {}",token, t.getMessage());
        return  Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackClearUserValidTokenCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackClearUserValidTokenCache")
    public void clearUserValidTokenCache(Long userId) {
        cacheUtil.deleteFromCache(CacheConstraints.ALL_TOKEN_KEY.getKey(userId));
        log.debug("Cache cleared for userId {}",  userId);
    }

    public void fallbackClearUserValidTokenCache(Long userId, Throwable t) {
        log.warn("Redis not available to clear cache for userId {}, ignoring. Error: {}", userId, t.getMessage());
    }

    @Override
    @CircuitBreaker(name = "redisBreaker", fallbackMethod = "fallbackClearTokenCache")
    @Retry(name = "redisRetry", fallbackMethod = "fallbackClearTokenCache")
    public void clearTokenCache(String token) {
        cacheUtil.deleteFromCache(CacheConstraints.TOKEN_KEY.getKey(token));
        log.debug("Cache cleared for token {}",  token);
    }

    public void fallbackClearTokenCache(String token, Throwable t) {
        log.warn("Redis not available to clear cache for token {}, ignoring. Error: {}", token, t.getMessage());
    }



}
