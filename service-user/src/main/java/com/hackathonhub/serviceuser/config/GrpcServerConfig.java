package com.hackathonhub.serviceuser.config;

import com.hackathonhub.serviceuser.grpc.UserGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcServerConfig {

    @Bean
    public Server grpcServer(UserGrpc.UserImplBase userService) {
        return ServerBuilder.forPort(55000).addService(userService).build();
    }
}



