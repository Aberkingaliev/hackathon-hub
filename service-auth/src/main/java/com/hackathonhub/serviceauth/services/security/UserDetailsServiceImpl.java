package com.hackathonhub.serviceauth.services.security;

import com.hackathonhub.serviceauth.mappers.grpc.user.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.grpc.UserGrpc;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @GrpcClient("service-user")
    private UserGrpc.UserBlockingStub userStub;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserGrpcService.UserRequest request = UserGrpcService.UserRequest
                .newBuilder()
                .setUserForGetByEmail(UserGrpcService.UserGetByEmailRequest
                        .newBuilder()
                        .setEmail(email)
                        .build())
                .setAction(UserGrpcService.actions_enum.getUserByEmail)
                .build();

        UserGrpcService.UserResponse response = userStub.getUserByEmail(request);

        if (!response.hasUser()) {
            log.error("User {} not found", email);
            throw new UsernameNotFoundException("User " + email + " not found");
        }

        return UserDetailsImpl
                .build(
                        UserMapperFactory
                        .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                        .fromGrpcResponseToLocal(response)
                );
    }
}
