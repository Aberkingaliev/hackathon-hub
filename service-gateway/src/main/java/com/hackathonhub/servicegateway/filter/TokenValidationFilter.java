package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.identity_protos.grpc.IdentityServiceGrpc;
import com.hackathonhub.identity_protos.grpc.Messages;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Configuration
@Slf4j
public class TokenValidationFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private IdentityServiceGrpc.IdentityServiceBlockingStub identityStub;

    @Override
    public Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(exchange)
                .flatMap(this::buildRequestMessage)
                .flatMap(this::validateTokens)
                .flatMap(response -> returnResults(exchange, chain, response))
                .switchIfEmpty(Mono.defer(() -> {
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse()
                            .setComplete();
                }))
                .onErrorResume(StatusRuntimeException.class, e -> {
                    log.error("Token validation failed: ", e);
                    exchange.getResponse()
                            .setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse()
                            .setComplete();
                });
    }

    private Mono<Void> returnResults(ServerWebExchange exchange,
                                     GatewayFilterChain chain,
                                     Messages.ValidateTokensResponse response) {
        if (!response.getIsValid()) {
            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus
                                    .valueOf(response.getStatus().getCode())
                    );
            return exchange.getResponse()
                    .setComplete();
        }

        return chain.filter(exchange);
    }

    private Mono<Messages.ValidateTokensResponse> validateTokens(Messages.ValidateTokensRequest request) {
        return Mono.just(request)
                .map(identityStub::validateTokens);
    }

    private Mono<Messages.ValidateTokensRequest> buildRequestMessage(ServerWebExchange exchange) {
        Messages.ValidateTokensRequest.Builder request = Messages.ValidateTokensRequest
                .newBuilder()
                .setRoute(exchange.getRequest().getPath().toString());

        Optional<String> accessToken = getAccessToken(exchange);
        Optional<String> refreshToken = getRefreshToken(exchange);


        return accessToken.isEmpty() || refreshToken.isEmpty()
                ? Mono.empty()
                : Mono.just(
                        request
                                .setAccessToken(accessToken.get())
                                .setRefreshToken(refreshToken.get())
                                .build()
        );
    }

    private Optional<String> getAccessToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange
                        .getRequest()
                        .getHeaders()
                        .getFirst("Authorization"))
                .map(header -> header.replace("Bearer ", ""));
    }

    private Optional<String> getRefreshToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange
                        .getRequest()
                        .getCookies()
                        .getFirst("refreshToken"))
                .map(HttpCookie::getValue);
    }


    @Override
    public int getOrder() {
        return -2;
    }
}
