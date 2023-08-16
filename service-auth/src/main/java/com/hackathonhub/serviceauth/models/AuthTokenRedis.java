package com.hackathonhub.serviceauth.models;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;


@Data
@RedisHash("authTokens")
public class AuthTokenRedis implements Serializable {

    protected UUID userId;

    protected String accessToken;

    protected String refreshToken;
}

