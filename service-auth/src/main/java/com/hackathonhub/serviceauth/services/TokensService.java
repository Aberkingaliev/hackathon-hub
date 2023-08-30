package com.hackathonhub.serviceauth.services;

import com.google.protobuf.Timestamp;
import com.hackathonhub.serviceauth.grpc.TokensGrpc;
import com.hackathonhub.serviceauth.grpc.TokensGrpcService;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


@GrpcService
public class TokensService extends TokensGrpc.TokensImplBase {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    AuthRepository authRepository;

    @Override
    public void tokensValidation(TokensGrpcService.TokenValidationRequest request,
                                 StreamObserver<TokensGrpcService.TokenValidationResponse> responseObserver) {
        String refreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        boolean isRefreshTokenValid = jwtUtils.validateToken(refreshToken);
        boolean isAccessTokenValid = jwtUtils.validateToken(accessToken);

        TokensGrpcService.TokenValidationResponse.Builder response =
                TokensGrpcService.TokenValidationResponse
                        .newBuilder()
                        .setIsRefreshTokenValid(isRefreshTokenValid)
                        .setIsAccessTokenValid(isAccessTokenValid);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void refreshTokens(TokensGrpcService.RefreshTokensRequest request,
                              StreamObserver<TokensGrpcService.RefreshTokensResponse> responseObserver) {
        String currentRefresh = request.getRefreshToken();
        String email = jwtUtils.getUserSubject(currentRefresh);

        AuthToken updatedTokens = generateAndUpdateTokens(email, currentRefresh);

        TokensGrpcService.AuthTokens mappedTokens = TokensGrpcService.AuthTokens
                .newBuilder()
                .setId(updatedTokens.getId().toString())
                .setUserId(updatedTokens.getUserId().toString())
                .setAccessToken(updatedTokens.getAccessToken())
                .setRefreshToken(updatedTokens.getRefreshToken())
                .setCreatedAt(
                        Timestamp.newBuilder()
                                .setNanos(updatedTokens
                                        .getCreatedAt()
                                        .getNanos())
                                .build()
                )
                .build();

        TokensGrpcService.RefreshTokensResponse.Builder response =
                TokensGrpcService.RefreshTokensResponse
                        .newBuilder()
                                .setUpdatedTokens(mappedTokens);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private AuthToken generateAndUpdateTokens(String email,
                                          String currentRefresh) {
        Set<RoleEnum> roles = jwtUtils.getRolesFromToken(currentRefresh);
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        HashMap<String, String> generatedTokens =
                jwtUtils.generateToken(email, authorities);

        AuthToken authToken = authRepository.findByRefreshToken(currentRefresh);

        authToken
                .setRefreshToken(generatedTokens.get("refreshToken"))
                .setAccessToken(generatedTokens.get("accessToken"));

        return authRepository
                .save(authToken);
    }
}
