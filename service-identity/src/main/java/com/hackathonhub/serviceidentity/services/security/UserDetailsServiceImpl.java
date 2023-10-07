package com.hackathonhub.serviceidentity.services.security;

import com.hackathonhub.common.grpc.Dto;
import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceidentity.dtos.UserDto;
import com.hackathonhub.serviceidentity.mappers.grpc.common.TypeMapper;
import com.hackathonhub.serviceidentity.mappers.grpc.common.UserDtoMapper;
import com.hackathonhub.serviceidentity.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceidentity.models.User;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@NoArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Messages.GetUserByEmailRequest request = Messages.GetUserByEmailRequest
                .newBuilder()
                .setEmail(email)
                .build();

        try {
            final Entities.User response = userStub.getUserEntityByEmail(request);

            final User mappedUser = UserEntityMapper.toEntity(response);

            return UserDetailsImpl
                    .build(mappedUser);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new UsernameNotFoundException("User not found: ", e);
            }
            throw new RuntimeException("Error while fetching user with email: ", e);
        }
    }

    /**
     * Loading UserDto(without password) by UUID
     * @param id - UUID of user
     */
    public UserDto loadUserById(UUID id) {
        final Messages.GetUserByIdRequest request = Messages.GetUserByIdRequest
                .newBuilder()
                .setId(TypeMapper.toGrpcUuid(id))
                .build();

        try {
            final Dto.UserDto response = userStub.getUserById(request);

            return UserDtoMapper.toOriginalDto(response);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new UsernameNotFoundException("User not found with id", e);
            }
            throw new RuntimeException("Error while fetching user with id", e);
        }
    }
}

