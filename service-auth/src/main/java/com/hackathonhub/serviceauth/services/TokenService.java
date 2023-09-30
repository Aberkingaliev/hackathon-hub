package com.hackathonhub.serviceauth.services;

import com.hackathonhub.auth_protos.grpc.Messages;
import com.hackathonhub.auth_protos.grpc.TokensServiceGrpc;
import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.mappers.grpc.common.AuthTokensEntityMapper;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@GrpcService
@Slf4j
public class TokenService extends TokensServiceGrpc.TokensServiceImplBase {
    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    AuthRepository authRepository;

    @Override
    public void validateTokens(Messages.ValidateTokensRequest request,
                               StreamObserver<Messages.ValidateTokensResponse> responseObserver) {
        try {
            Messages.ValidateTokensResponse response = Messages.ValidateTokensResponse
                    .newBuilder()
                    .setIsRefreshTokenValid(jwtUtils.validateToken(request.getRefreshToken()))
                    .setIsAccessTokenValid(jwtUtils.validateToken(request.getAccessToken()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while validating tokens: ", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateTokens
            (Messages.UpdateTokensRequest request, StreamObserver<Entities.AuthTokens> responseObserver) {
        String email = jwtUtils.getUserSubject(request.getRefreshToken());

        Map<String, String> generatedTokens = generateNewTokens(email, request.getRefreshToken());
        try {
            AuthToken updatedTokens = updateTokens(request.getRefreshToken(), generatedTokens);

            Entities.AuthTokens mappedTokens = AuthTokensEntityMapper.toGrpcEntity(updatedTokens);

            responseObserver.onNext(mappedTokens);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.info("Error while updating tokens: ", e);
            responseObserver.onError(e);
        }
    }

    private Map<String, String> generateNewTokens(String email,
                                                  String currentRefresh) {
        Set<RoleEnum> roles = jwtUtils.getRolesFromToken(currentRefresh);
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());

        return jwtUtils.generateToken(email, authorities);
    }

    private AuthToken updateTokens(String currentRefresh, Map<String, String> generatedTokens)
            throws StatusException {
        return authRepository.findByRefreshToken(currentRefresh).map(
                        token -> {
                            token.setAccessToken(generatedTokens.get("accessToken"));
                            token.setRefreshToken(generatedTokens.get("refreshToken"));
                            return authRepository.save(token);
                        })
                .orElseThrow(() -> new StatusException(Status.NOT_FOUND.withDescription("Token not found")));

    }
}
