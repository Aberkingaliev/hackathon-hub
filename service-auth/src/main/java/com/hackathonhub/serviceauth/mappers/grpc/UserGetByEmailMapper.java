package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceauth.models.Role;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class UserGetByEmailMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        User user = context.getUserData().get();

        List<UserGrpcService.UserRole> userRoles = user.getRoles()
                .stream()
                .map(role -> UserGrpcService.UserRole
                        .newBuilder()
                        .setId(role.getId().toString())
                        .setRole(
                                UserGrpcService.role_enum.valueOf(
                                        role.getRole_name().toString()
                                )
                        )
                        .build())
                .toList();



        UserGrpcService.UserResponseData data = UserGrpcService.UserResponseData
                .newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .setTeamId(user.getTeamId().toString())
                .addAllRoles(userRoles)
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
        HashSet<Role> roles = new HashSet<>(
                user
                        .getRolesList()
                        .stream()
                        .map(role -> new Role()
                                .setId(UUID.fromString(role.getId()))
                                .setRole_name(
                                        RoleEnum.valueOf(
                                                role.getRole().toString()
                                        )
                                )
                        )
                        .toList());
        return new User()
                .setId(UUID.fromString(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(UUID.fromString(user.getTeamId()))
                .setRole(roles);

    }
}
