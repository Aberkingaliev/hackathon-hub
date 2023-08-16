package com.hackathonhub.serviceuser.mappers.grpc.strategies;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.models.User;

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
