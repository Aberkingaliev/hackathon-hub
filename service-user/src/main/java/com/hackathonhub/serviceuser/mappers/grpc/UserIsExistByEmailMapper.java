package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;

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
