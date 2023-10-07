package com.hackathonhub.serviceidentity.config.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JWTProperties {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.accessTokenExpiration}")
    private String accessTokenExpiration;
    @Value("${jwt.refreshTokenExpiration}")
    private String refreshTokenExpiration;
}
