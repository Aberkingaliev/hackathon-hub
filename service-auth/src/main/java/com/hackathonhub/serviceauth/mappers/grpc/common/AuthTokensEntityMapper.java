package com.hackathonhub.serviceauth.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.models.AuthToken;

public class AuthTokensEntityMapper {

    public static AuthToken toEntity (Entities.AuthTokens authTokens) {
        return new AuthToken()
                .setId(TypeMapper.toOriginalyUuid(authTokens.getId()))
                .setUserId(TypeMapper.toOriginalyUuid(authTokens.getUserId()))
                .setAccessToken(authTokens.getAccessToken())
                .setRefreshToken(authTokens.getRefreshToken())
                .setCreatedAt(TypeMapper.toOriginalyTimestamp(authTokens.getCreatedAt()));
    }


    public static Entities.AuthTokens toGrpcEntity (AuthToken authToken) {
        return Entities.AuthTokens.newBuilder()
                .setId(TypeMapper.toGrpcUuid(authToken.getId()))
                .setUserId(TypeMapper.toGrpcUuid(authToken.getUserId()))
                .setAccessToken(authToken.getAccessToken())
                .setRefreshToken(authToken.getRefreshToken())
                .setCreatedAt(TypeMapper.toGrpcTimestamp(authToken.getCreatedAt()))
                .build();
    }
}
