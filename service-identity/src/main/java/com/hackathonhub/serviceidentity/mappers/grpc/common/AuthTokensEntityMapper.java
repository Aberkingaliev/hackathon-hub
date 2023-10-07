package com.hackathonhub.serviceidentity.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceidentity.models.AuthToken;

public class AuthTokensEntityMapper {

    public static AuthToken toEntity (Entities.AuthTokens authTokens) {
        return new AuthToken()
                .setId(TypeMapper.toOriginallyUuid(authTokens.getId()))
                .setUserId(TypeMapper.toOriginallyUuid(authTokens.getUserId()))
                .setAccessToken(authTokens.getAccessToken())
                .setRefreshToken(authTokens.getRefreshToken())
                .setCreatedAt(TypeMapper.toOriginallyTimestamp(authTokens.getCreatedAt()));
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
