package com.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class JwtUtil {

    public static final String BEARER ="Bearer ";

    public static Claims parseToken(String token, Key signKey){
        return Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
