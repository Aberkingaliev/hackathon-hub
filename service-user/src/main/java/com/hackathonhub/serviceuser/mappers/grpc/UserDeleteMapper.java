package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;

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
