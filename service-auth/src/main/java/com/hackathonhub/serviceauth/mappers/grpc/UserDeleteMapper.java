package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;

public class UserDeleteMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        return UserGrpcService.UserResponse
                .newBuilder()
                .setStatus(context.getStatus())
                .setMessage(context.getMessage())
                .setAction(UserGrpcService.actions_enum.deleteUser)
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        UserGrpcService.UserDeleteByIdRequest request = UserGrpcService.UserDeleteByIdRequest
                .newBuilder()
                .setId(context.getUserId().toString())
                .build();
        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.deleteUser)
                .setUserForDelete(request)
                .build();
    }
}
