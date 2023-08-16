package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.User;

import java.util.UUID;

public class UserGetByEmailMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        User user = context.getUserData().get();

        UserGrpcService.UserResponseData data = UserGrpcService.UserResponseData
                .newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setIsActivated(user.getIsActivated())
                .setTeamId(user.getTeamId().toString())
                .setRole(UserGrpcService.role_enum.valueOf(user.getRole().toString()))
                .build();

        return UserGrpcService.UserResponse
                .newBuilder()
                    .setStatus(context.getStatus())
                    .setMessage(context.getMessage())
                    .setUser(data)
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        UserGrpcService.UserGetByEmailRequest data = UserGrpcService.UserGetByEmailRequest
                .newBuilder()
                .setEmail(context.getUserEmail().get())
                .build();

        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.getUserByEmail)
                .setUserForGetByEmail(data)
                .build();
    }

    @Override
    public User fromGrpcResponseToLocal(UserGrpcService.UserResponse userResponse) {
        UserGrpcService.UserResponseData user = userResponse.getUser();
        return new User()
                .setId(UUID.fromString(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setActivated(user.getIsActivated())
                .setTeamId(UUID.fromString(user.getTeamId()))
                .setRole(Role.valueOf(user.getRole().toString()));
    }
}
