package com.hackathonhub.serviceidentity.config.auth;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;

import javax.annotation.Nullable;

/**
 * UNUSED BEAN
 */
@Configuration
@SuppressWarnings("unused")
public class GrpcAuthReader implements GrpcAuthenticationReader {

    @Nullable
    @Override
    public Authentication readAuthentication(ServerCall<?, ?> call, Metadata headers)
            throws AuthenticationException {
        return null;
    }
}
