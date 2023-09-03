package com.hackathonhub.servicegateway.filter;

import com.hackathonhub.auth_protos.grpc.AuthorityServiceGrpc;
import com.hackathonhub.auth_protos.grpc.Messages;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
public class AuthorityCheckFilter extends BaseFilter {

    @GrpcClient("service-auth")
    private AuthorityServiceGrpc.AuthorityServiceBlockingStub authorityStub;

    @Override
    public Mono<Void> customFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization")
                .substring(7);

        if(token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Messages.CheckAuthorityRequest request = Messages.CheckAuthorityRequest
                .newBuilder()
                .setAccessToken(token)
                .setRoute(exchange.getRequest().getPath().toString())
                .build();

        Messages.CheckAuthorityResponse response = authorityStub.checkAuthority(request);

        if(!response.getHasAccess()) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
