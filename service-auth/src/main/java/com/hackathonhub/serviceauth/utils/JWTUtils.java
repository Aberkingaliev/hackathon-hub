package com.hackathonhub.serviceauth.utils;


import com.hackathonhub.serviceauth.dtos.UserPayload;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JWTUtils {


    @Value("${jwt.secretKey}")
    private String secretKey;

    public Claims getClaimsFromToken(String token) {
        String parseSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts
                .parserBuilder()
                .setSigningKey(parseSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        return getClaimsFromToken(token)
                .getExpiration()
                .after(new Date());
    }

    public HashMap<String, String> generateToken (UserGrpcService.UserResponseData user) {
        HashMap<String, String> claims = new UserPayload()
                .setId(UUID.fromString(user.getId()))
                .setEmail(user.getEmail())
                .setIsActivate(user.getIsActivated())
                .setRole(user.getRole())
                .toHashMap();

        long expirationSeconds = Long.parseLong(String.valueOf(86400));
        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);

        return new HashMap<String, String>(
                Map.ofEntries(
                        Map.entry("accessToken", Jwts
                                .builder()
                                .setClaims(claims)
                                .setSubject(claims.get("id"))
                                .setIssuedAt(creationDate)
                                .setExpiration(expirationDate)
                                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                                .compact()),
                        Map.entry("refreshToken", Jwts
                                .builder()
                                .setClaims(claims)
                                .setSubject(claims.get("id"))
                                .setIssuedAt(creationDate)
                                .setExpiration(expirationDate)
                                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                                .compact())
                )
        );
    }

    public UUID getUserSubject (String token ) {
        return UUID.fromString(getClaimsFromToken(token).getSubject());
    }
}
