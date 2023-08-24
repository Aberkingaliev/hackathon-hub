package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.UUID;
import java.util.List;


@Slf4j
public class UserGetByEmailMapper implements UserMapperStrategy {
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
                    .setUser(data)
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        UserGrpcService.UserGetByEmailRequest data = UserGrpcService.UserGetByEmailRequest
                .newBuilder()
                .setEmail(context.getUserEmail().orElseThrow(
                        () -> {
                            log.error("EMAIL_NOT_FOUND_FOR_MAPPING: " + context.getUserEmail());
                            return new RuntimeException("EMAIL_NOT_FOUND_FOR_MAPPING" + context.getUserEmail());
                        }
                ))
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
                user.getRolesList()
                        .stream()
                        .map(role -> new Role()
                                .setId(UuidUtils.stringToUUID(role.getId()))
                                .setRole_name(
                                        RoleEnum.valueOf(
                                                role.getRole()
                                                .toString()
                                        )
                                )
                        )
                        .toList()
        );
        return new User()
                .setId(UuidUtils.stringToUUID(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(UuidUtils.stringToUUID(user.getTeamId()))
                .setRole(roles);

    }
}
