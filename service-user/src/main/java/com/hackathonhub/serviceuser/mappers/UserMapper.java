package com.hackathonhub.serviceuser.mappers;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.User;
import java.util.UUID;


public class UserMapper {

    public static User mapGrpcToLocal(UserGrpcService.UserSaveRequest user) {
        return new User()
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(UUID.fromString(user.getTeamId()))
                .setRole(Role.valueOf(user.getRole().toString()));
    }

    public static UserGrpcService.UserResponse mapLocalToGrpc(UserMapperGrpcActions actionType, UserResponseContext context) {
        UserGrpcService.UserResponse.Builder response = UserGrpcService.UserResponse
                .newBuilder()
                .setStatus(context.getStatus())
                .setMessage(context.getMessage());

        switch (actionType) {
            case save, getByEmail -> context.getUser().ifPresent(u -> response.setUser(buildUserResponse(u)));
            case existByEmail -> context.getIsExist().ifPresent(response::setIsUserAlreadyExist);
            case delete -> response.clearUser();
        }

        return response.build();
    }

    private static UserGrpcService.UserResponseData buildUserResponse(User user) {
        return UserGrpcService
                .UserResponseData
                .newBuilder()
                    .setId(user.getId().toString())
                    .setUsername(user.getUsername())
                    .setFullName(user.getFullName())
                    .setEmail(user.getEmail())
                    .setIsActivated(user.getIsActivated())
                    .setTeamId(user.getTeamId().toString())
                    .setRole(UserGrpcService.role_enum.valueOf(user.getRole().toString()))
                .build();
    }
}
