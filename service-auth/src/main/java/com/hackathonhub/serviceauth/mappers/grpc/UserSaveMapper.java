package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceauth.models.Role;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UserSaveMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        User user = context.getUserData().orElseThrow(()-> {
            log.error("USER_NOT_FOUND_FOR_MAPPING: " + context.getUserData());
            return new RuntimeException("USER_NOT_FOUND_FOR_MAPPING: " + context.getUserData());
        });

        List<UserGrpcService.UserRole> userRoles = user.getRoles()
                .stream()
                .map(role -> UserGrpcService.UserRole
                        .newBuilder()
                        .setId(UuidUtils.uuidToString(role.getId()))
                        .setRole(
                                UserGrpcService.role_enum.valueOf(
                                        role.getRole_name().toString()
                                )
                        )
                        .build())
                .toList();

        UserGrpcService.UserResponseData data = UserGrpcService.UserResponseData
                .newBuilder()
                .setId(UuidUtils.uuidToString(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .setTeamId(UuidUtils.uuidToString(user.getTeamId()))
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
        User user = context.getUserData().orElseThrow(()-> {
            log.error("USER_NOT_FOUND_FOR_MAPPING: " + context.getUserData());
            return new RuntimeException("USER_NOT_FOUND_FOR_MAPPING: " + context.getUserData());
        });;

        List<UserGrpcService.UserRole> userRoles = user
                .getRoles()
                .stream()
                .map(role -> UserGrpcService.UserRole.newBuilder()
                        .setId(UuidUtils.uuidToString(role.getId()))
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
                .setTeamId(UuidUtils.uuidToString(user.getTeamId()))
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
                                .setId(UuidUtils.stringToUUID(role.getId()))
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
                .setTeamId(UuidUtils.stringToUUID(user.getTeamId()))
                .setRole(roles);
    }

    @Override
    public User fromGrpcResponseToLocal(UserGrpcService.UserResponse userResponse) {
        UserGrpcService.UserResponseData user = userResponse.getUser();

        HashSet<Role> roles = new HashSet<>(
                user.getRolesList()
                        .stream()
                        .map(role -> new Role()
                                .setId(UuidUtils.stringToUUID(role.getId()))
                                .setRole_name(
                                        RoleEnum.valueOf(
                                                role.getRole().toString()
                                        )
                                )
                        ).toList()
        );
        return new User()
                .setId(UuidUtils.stringToUUID(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(UUID.fromString(user.getTeamId()))
                .setRole(roles);
    }
}
