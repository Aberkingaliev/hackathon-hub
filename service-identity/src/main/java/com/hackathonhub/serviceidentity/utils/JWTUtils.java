package com.hackathonhub.serviceidentity.utils;

import com.hackathonhub.serviceidentity.config.auth.JWTProperties;
import com.hackathonhub.serviceidentity.models.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JWTUtils {

    private final JWTProperties properties;

    @Autowired
    public JWTUtils(JWTProperties properties) {
        this.properties = properties;
    }


    public Key getSignKey() {
        return Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .after(new Date());
    }

    public String generateAccessToken(Collection<? extends GrantedAuthority> authorities,
                                      String email) {
        Map<String, Object> claims = new HashMap<>(
                Map.of("roles", authorities
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
        );

        return tokenBuild(claims, email,
                Long.parseLong(properties.getAccessTokenExpiration()));
    }

    public String generateRefreshToken(String email) {
        return tokenBuild(new HashMap<>(), email,
                Long.parseLong(properties.getRefreshTokenExpiration()));
    }

    private String tokenBuild(Map<String, Object> claims,
                              String email,
                              Long expirations) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirations))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .addClaims(claims)
                .compact();
    }

    public Set<RoleEnum> extractRolesFromToken(String token) {
        List<String> authorities = extractAllClaims(token).get("roles", List.class);

        return authorities == null
                ? Collections.emptySet()
                : authorities
                .stream()
                .map(RoleEnum::valueOf)
                .collect(Collectors.toSet());

    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }
}
