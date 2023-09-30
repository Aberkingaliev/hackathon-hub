package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.auth_protos.grpc.Messages;
import com.hackathonhub.auth_protos.grpc.TokensServiceGrpc;
import com.hackathonhub.common.grpc.Entities;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Configuration
public class TokenValidationFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private TokensServiceGrpc.TokensServiceBlockingStub tokensStub;

    @Override
    public Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return validateTokens(exchange)
                .flatMap(isValid -> isValid ? proceed(chain, exchange) : unauthorized(exchange))
                .switchIfEmpty(refreshTokens(exchange)
                        .flatMap(newTokens -> updateExchangeAndProceed(newTokens, exchange, chain))
                        .switchIfEmpty(Mono.defer(() -> unauthorized(exchange))));
    }

    private Mono<Boolean> validateTokens(ServerWebExchange exchange) {
        Optional<String> accessToken = extractAccessToken(exchange);
        Optional<String> refreshToken = extractRefreshToken(exchange);

        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            return Mono.empty();
        }

        Messages.ValidateTokensRequest request = Messages.ValidateTokensRequest.newBuilder()
                .setAccessToken(accessToken.get())
                .setRefreshToken(refreshToken.get())
                .build();

        Messages.ValidateTokensResponse response = tokensStub.validateTokens(request);
        return Mono.just(response.getIsAccessTokenValid());
    }

    private Optional<String> extractAccessToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization"))
                .map(a -> a.substring(7));
    }

    private Optional<String> extractRefreshToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest()
                        .getCookies()
                        .getFirst("refreshToken"))
                .map(HttpCookie::getValue);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> proceed(GatewayFilterChain chain, ServerWebExchange exchange) {
        return chain.filter(exchange);
    }

    private Mono<Map<String, String>> refreshTokens(ServerWebExchange exchange) {
        Optional<String> refreshToken = extractRefreshToken(exchange);

        if (refreshToken.isEmpty()) {
            return Mono.empty();
        }

        Messages.UpdateTokensRequest refreshRequest = Messages.UpdateTokensRequest
                .newBuilder()
                .setRefreshToken(refreshToken.get())
                .build();

        Entities.AuthTokens refreshResponse = tokensStub.updateTokens(refreshRequest);

        Map<String, String> newTokens = new HashMap<>();
        newTokens.put("accessToken", refreshResponse.getAccessToken());
        newTokens.put("refreshToken", refreshResponse.getRefreshToken());

        return Mono.just(newTokens);
    }

    private Mono<Void> updateExchangeAndProceed(Map<String, String> newTokens,
                                                ServerWebExchange exchange,
                                                GatewayFilterChain chain) {
        ServerWebExchange mutatedExchange = mutateExchange(newTokens, exchange);
        return chain.filter(mutatedExchange);
    }

    private ServerWebExchange mutateExchange(Map<String, String> newTokens,
                                             ServerWebExchange exchange) {
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .headers(h -> h.setBearerAuth(newTokens.get("accessToken")))
                        .build())
                .build();

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newTokens.get("refreshToken"))
                .path("/")
                .build();

        mutatedExchange.getResponse().getCookies().add("refreshToken", cookie);
        mutatedExchange.getResponse().getHeaders().setBearerAuth(newTokens.get("accessToken"));

        return mutatedExchange;
    }


    @Override
    public int getOrder() {
        return -2;
    }
}
