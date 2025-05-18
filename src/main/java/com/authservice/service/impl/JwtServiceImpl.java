package com.authservice.service.impl;

import com.authservice.exception.ExceptionConstants;
import com.authservice.exception.UnAuthorizedException;
import com.authservice.mapper.TokenMapper;
import com.authservice.security.UserPrincipal;
import com.authservice.service.JwtService;
import com.authservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public Optional<String> extractUsername(String token) {
        return Optional.ofNullable(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserPrincipal user) {
        return TokenMapper.toJwt(user, new Date(),
                Date.from(Instant.now().plus(24, ChronoUnit.HOURS)),getSignKey(), SignatureAlgorithm.HS256);
    }

    @Override
    public String generateRefreshToken(UserPrincipal user) {
        return TokenMapper.toJwt(user, new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + refreshExpiration),getSignKey(), SignatureAlgorithm.HS256);
    }

    @Override
    public boolean isTokenValid(String token, UserPrincipal user) {
        final String username = extractUsername(token).orElseThrow(() ->
                new UnAuthorizedException(ExceptionConstants.INVALID_TOKEN.getMessage()));
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return JwtUtil.parseToken(token,getSignKey());
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
