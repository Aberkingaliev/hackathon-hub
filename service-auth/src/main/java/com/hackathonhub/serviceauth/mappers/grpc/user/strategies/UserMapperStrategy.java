package com.hackathonhub.serviceauth.mappers.grpc.user.strategies;

import com.hackathonhub.serviceauth.mappers.grpc.user.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.user.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;

public interface UserMapperStrategy {
    UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context);

    UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context);
    default User fromGrpcRequestToLocal(UserGrpcService.UserRequest userRequest) {
        return null;
    };
    default User fromGrpcResponseToLocal(UserGrpcService.UserResponse userResponse) {
        return null;
    };
}
