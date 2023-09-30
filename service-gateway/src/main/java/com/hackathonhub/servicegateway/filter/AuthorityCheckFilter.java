package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.auth_protos.grpc.AuthorityServiceGrpc;
import com.hackathonhub.auth_protos.grpc.Messages;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Configuration
public class AuthorityCheckFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private AuthorityServiceGrpc.AuthorityServiceBlockingStub authorityStub;

    @Override
    public Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return extractToken(exchange)
                .flatMap(token -> checkAuthority(token, exchange))
                .flatMap(hasAccess -> proceedOrTerminate(hasAccess, exchange, chain))
                .switchIfEmpty(Mono.defer(() -> unauthorized(exchange)));
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .map(token -> token.substring(7))
                .map(Mono::just)
                .orElse(Mono.empty());
    }

    private Mono<Boolean> checkAuthority(String token, ServerWebExchange exchange) {
        Messages.CheckAuthorityRequest request = Messages.CheckAuthorityRequest.newBuilder()
                .setAccessToken(token)
                .setRoute(exchange.getRequest().getPath().toString())
                .build();
        Messages.CheckAuthorityResponse response = authorityStub.checkAuthority(request);
        return Mono.just(response.getHasAccess());
    }

    private Mono<Void> proceedOrTerminate(boolean hasAccess, ServerWebExchange exchange, GatewayFilterChain chain) {
        if (hasAccess) {
            return chain.filter(exchange);
        } else {
            return forbidden(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
