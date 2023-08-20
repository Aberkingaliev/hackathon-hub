package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;

public class UserIsExistByEmailMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        return UserGrpcService.UserResponse.newBuilder()
                .setStatus(context.getStatus())
                .setMessage(context.getMessage())
                .setIsUserAlreadyExist(context.getIsExistState().get())
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        UserGrpcService.UserIsExistByEmailRequest request = UserGrpcService.UserIsExistByEmailRequest
                .newBuilder()
                .setEmail(context.getUserEmail().get())
                .build();

        return UserGrpcService.UserRequest
                .newBuilder()
                .setUserIsExistByEmail(request).build();
    }

}
