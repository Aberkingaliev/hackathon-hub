package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.auth_protos.grpc.Messages;
import com.hackathonhub.auth_protos.grpc.TokensServiceGrpc;
import com.hackathonhub.common.grpc.Entities;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
public class TokenValidationFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private TokensServiceGrpc.TokensServiceBlockingStub tokensStub;

    @Override
    public Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = exchange
                .getRequest()
                .getHeaders()
                .getFirst("Authorization").substring(7);
        String refreshToken = exchange
                .getRequest()
                .getCookies()
                .getFirst("refreshToken").getValue();

        Messages.ValidateTokensRequest request = Messages.ValidateTokensRequest
                .newBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();

        Messages.ValidateTokensResponse response = tokensStub.validateTokens(request);

        if(!response.getIsRefreshTokenValid()) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if(response.getIsAccessTokenValid()) {
            return chain.filter(exchange);
        }

        Messages.UpdateTokensRequest refreshRequest = Messages.UpdateTokensRequest
                .newBuilder()
                .setRefreshToken(refreshToken)
                .build();

        Entities.AuthTokens refreshResponse = tokensStub.updateTokens(refreshRequest);

        ServerWebExchange mutatedExchange = exchange
                .mutate()
                .request(exchange.getRequest()
                            .mutate()
                            .headers(h -> h.setBearerAuth(refreshResponse.getAccessToken()))
                            .build())
                .build();

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshResponse.getRefreshToken())
                .path("/")
                .build();

        mutatedExchange.getResponse().getCookies().add("refreshToken", cookie);
        mutatedExchange.getResponse()
                .getHeaders()
                .setBearerAuth(refreshResponse.getAccessToken());

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
