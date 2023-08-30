package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.serviceauth.grpc.TokensGrpc;
import com.hackathonhub.serviceauth.grpc.TokensGrpcService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
public class TokenValidationFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private TokensGrpc.TokensBlockingStub tokensStub;

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

        TokensGrpcService.TokenValidationRequest request =
                TokensGrpcService.TokenValidationRequest.newBuilder()
                        .setAccessToken(accessToken)
                        .setRefreshToken(refreshToken)
                        .build();

        TokensGrpcService.TokenValidationResponse response = tokensStub.tokensValidation(request);

        if(!response.getIsRefreshTokenValid()) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if(response.getIsAccessTokenValid()) {
            return chain.filter(exchange);
        }

        TokensGrpcService.RefreshTokensRequest refreshRequest =
                TokensGrpcService.RefreshTokensRequest.newBuilder()
                        .setRefreshToken(refreshToken)
                        .build();

        TokensGrpcService.RefreshTokensResponse refreshResponse =
                tokensStub.refreshTokens(refreshRequest);

        TokensGrpcService.AuthTokens updatedTokens = refreshResponse.getUpdatedTokens();

        ServerWebExchange mutatedExchange = exchange
                .mutate()
                .request(exchange.getRequest()
                            .mutate()
                            .headers(h -> h.setBearerAuth(updatedTokens.getAccessToken()))
                            .build())
                .build();

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", updatedTokens.getRefreshToken())
                .path("/")
                .build();

        mutatedExchange.getResponse().getCookies().add("refreshToken", cookie);
        mutatedExchange.getResponse()
                .getHeaders()
                .setBearerAuth(updatedTokens.getAccessToken());

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
