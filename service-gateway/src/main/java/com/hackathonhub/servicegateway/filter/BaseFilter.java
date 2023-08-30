package com.hackathonhub.servicegateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseFilter implements GlobalFilter, Ordered {

    private final Set<String> excludeRoutes;

    public BaseFilter() {
        excludeRoutes = new HashSet<>(
                Set.of(
                        "/service-auth/api/login",
                        "/service-auth/api/registration"
                )
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String route = exchange.getRequest().getPath().value();
        if(excludeRoutes.contains(route)) {
            return chain.filter(exchange);
        }
        return customFilter(exchange, chain);
    }

    public abstract Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain);
}
