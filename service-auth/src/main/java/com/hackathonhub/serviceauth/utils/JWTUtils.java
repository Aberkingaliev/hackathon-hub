package com.hackathonhub.serviceauth.utils;

import com.hackathonhub.serviceauth.models.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final String BEARER_PREFIX = "Bearer ";

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public String getParsedSecretKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getParsedSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            return getClaimsFromToken(token)
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            log.error("Jwt util validate: " + e.getMessage());
            return false;
        }
    }

    public HashMap<String, String> generateToken (String email,
                                                  Collection<? extends GrantedAuthority> authorities) {

        Date currentTime = new Date();

        long fifteenMinutesMillis = TimeUnit.MINUTES.toMillis(1);
        Date accessTokenExpiration =
                new Date(currentTime.getTime() + fifteenMinutesMillis);

        long thirtyDaysMillis = TimeUnit.HOURS.toMillis(15);
        Date refreshTokenExpiration =
                new Date(currentTime.getTime() + thirtyDaysMillis);


        Set<String> authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        return new HashMap<String, String>(
                Map.ofEntries(
                        Map.entry("accessToken", Jwts
                                .builder()
                                .setClaims(Map.of("authorities", authoritiesList))
                                .setSubject(email)
                                .setIssuedAt(currentTime)
                                .setExpiration(accessTokenExpiration)
                                .signWith(Keys
                                        .hmacShaKeyFor(secretKey.getBytes()))
                                .compact()),
                        Map.entry("refreshToken", Jwts
                                .builder()
                                .setClaims(Map.of("authorities", authoritiesList))
                                .setSubject(email)
                                .setIssuedAt(currentTime)
                                .setExpiration(refreshTokenExpiration)
                                .signWith(Keys
                                        .hmacShaKeyFor(secretKey.getBytes()))
                                .compact())
                )
        );
    }

    public Set<RoleEnum> getRolesFromToken(String token) {
        List<String> authoritiesFromToken =
                (List<String>)
                getClaimsFromToken(token)
                        .get("authorities");


        return authoritiesFromToken
                .stream()
                .map(RoleEnum::valueOf)
                .collect(Collectors.toSet());
    }

    public String getUserSubject(String token) {
        return  Jwts
                .parserBuilder()
                .setSigningKey(getParsedSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }


    public String parseAccessToken(HttpServletRequest request) {
        String authBearer = request.getHeader("Authorization");

        if (authBearer != null
                && authBearer.startsWith(BEARER_PREFIX)) {
            return authBearer.substring(BEARER_PREFIX.length());
        }

        return null;
    }


}
