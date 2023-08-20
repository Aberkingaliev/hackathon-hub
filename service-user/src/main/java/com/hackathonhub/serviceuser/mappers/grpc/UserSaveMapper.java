package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import com.hackathonhub.serviceuser.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class UserSaveMapper implements UserMapperStrategy {
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
                .setAction(UserGrpcService.actions_enum.saveUser)
                .setUser(data)
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        User user = context.getUserData().get();

        List<UserGrpcService.UserRole> userRoles = user
                .getRoles()
                .stream()
                .map(role -> UserGrpcService.UserRole.newBuilder()
                        .setId(role.getId().toString())
                        .setRole(
                                UserGrpcService.role_enum.valueOf(
                                        role.getRole_name().toString()
                                )
                        )
                        .build())
                .toList();

        UserGrpcService.UserSaveRequest request = UserGrpcService.UserSaveRequest
                .newBuilder()
                    .setUsername(user.getUsername())
                    .setFullName(user.getFullName())
                    .setEmail(user.getEmail())
                    .setPassword(user.getPassword())
                    .setIsActivated(user.getIsActivated())
                    .setTeamId(user.getTeamId().toString())
                    .addAllRoles(userRoles)
                .build();

        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.saveUser)
                .setUserForSave(request)
                .build();
    }

    @Override
    public User fromGrpcRequestToLocal(UserGrpcService.UserRequest userRequest) {
        UserGrpcService.UserSaveRequest user = userRequest.getUserForSave();

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
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(UUID.fromString(user.getTeamId()))
                .setRole(roles);
    }

    @Override
    public User fromGrpcResponseToLocal(UserGrpcService.UserResponse userResponse) {
        UserGrpcService.UserResponseData user = userResponse.getUser();

        HashSet<Role> roles = new HashSet<>(
                user.getRolesList()
                        .stream()
                        .map(role -> new Role()
                                .setId(UUID.fromString(role.getId()))
                                .setRole_name(
                                        RoleEnum.valueOf(
                                                role.getRole().toString()
                                        )
                                )
                        ).toList()
        );
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
