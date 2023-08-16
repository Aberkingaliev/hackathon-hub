package com.hackathonhub.serviceauth.models;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
public class AuthTokenRedis implements Serializable {

    protected UUID userId;

    protected String accessToken;

    protected String refreshToken;
}

