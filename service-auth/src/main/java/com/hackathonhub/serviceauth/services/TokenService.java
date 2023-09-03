package com.hackathonhub.serviceauth.services;

import com.hackathonhub.auth_protos.grpc.Messages;
import com.hackathonhub.auth_protos.grpc.TokensServiceGrpc;
import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.mappers.grpc.common.AuthTokensEntityMapper;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenService extends TokensServiceGrpc.TokensServiceImplBase {
    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    AuthRepository authRepository;

    @Override
    public void validateTokens(Messages.ValidateTokensRequest request,
                               StreamObserver<Messages.ValidateTokensResponse> responseObserver) {
        boolean isRefreshTokenValid = jwtUtils.validateToken(request.getRefreshToken());
        boolean isAccessTokenValid = jwtUtils.validateToken(request.getAccessToken());

        Messages.ValidateTokensResponse response = Messages.ValidateTokensResponse
                .newBuilder()
                .setIsRefreshTokenValid(isRefreshTokenValid)
                .setIsAccessTokenValid(isAccessTokenValid)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTokens(Messages.UpdateTokensRequest request, StreamObserver<Entities.AuthTokens> responseObserver) {
        String email = jwtUtils.getUserSubject(request.getRefreshToken());

        HashMap<String, String> generatedTokens = generateNewTokens(email, request.getRefreshToken());
        AuthToken updatedTokens = updateTokens(request.getRefreshToken(), generatedTokens);

        Entities.AuthTokens mappedTokens = AuthTokensEntityMapper.toGrpcEntity(updatedTokens);

        responseObserver.onNext(mappedTokens);
        responseObserver.onCompleted();
    }

    private HashMap<String, String> generateNewTokens(String email,
                                                      String currentRefresh) {
        Set<RoleEnum> roles = jwtUtils.getRolesFromToken(currentRefresh);
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        return jwtUtils.generateToken(email, authorities);
    }

    private AuthToken updateTokens(String currentRefresh, HashMap<String, String> generatedTokens) {
        AuthToken authToken = authRepository.findByRefreshToken(currentRefresh);

        authToken
                .setRefreshToken(generatedTokens.get("refreshToken"))
                .setAccessToken(generatedTokens.get("accessToken"));

        return authRepository
                .save(authToken);
    }
}
