package com.hackathonhub.serviceauth.services.security;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Messages.GetUserByEmailRequest request = Messages.GetUserByEmailRequest
                .newBuilder()
                .setEmail(email)
                .build();

        try {
            Entities.User response = userStub.getUserEntityByEmail(request);

            User mappedUser = UserEntityMapper.toEntity(response);

            return UserDetailsImpl
                    .build(mappedUser);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            throw new RuntimeException("Error while fetching user with email: " + email);
        }
    }
}

