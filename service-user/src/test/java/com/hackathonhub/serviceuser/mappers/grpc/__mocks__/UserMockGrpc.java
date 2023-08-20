package com.hackathonhub.serviceuser.mappers.grpc.__mocks__;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;

import java.util.Optional;

public class UserMockGrpc {

    public static UserGrpcService.UserResponse getUserForResponse() {
        UserResponseContext context = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .userData(Optional.of(UserMockLocal.getUserForResponse()))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromLocalToGrpcResponse(context);
    }

    public static UserGrpcService.UserRequest getUserSaveRequest() {
        UserRequestContext context = UserRequestContext.builder().userData(Optional.of(UserMockLocal.getUserForRequest())).build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromLocalToGrpcRequest(context);
    }

    public static UserGrpcService.UserRequest getUserByEmailRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userEmail(Optional.of(UserMockLocal.getUserForRequest().getEmail()))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                .fromLocalToGrpcRequest(context);
    }

    public static UserGrpcService.UserResponse getUserByEmailResponse() {
        UserResponseContext context = UserResponseContext.builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .userData(Optional.of(UserMockLocal.getUserForResponse()))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                .fromLocalToGrpcResponse(context);
    }

    public static UserGrpcService.UserRequest getUserDeleteRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userId(Optional.of(UserMockLocal.getUserForResponse().getId()))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.deleteUser)
                .fromLocalToGrpcRequest(context);
    }

    public static UserGrpcService.UserResponse getUserDeleteResponse() {
        UserResponseContext context = UserResponseContext.builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.deleteUser)
                .fromLocalToGrpcResponse(context);
    }

    public static UserGrpcService.UserRequest getUserIsExistByEmailRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userEmail(Optional.of(UserMockLocal.getUserForResponse().getEmail()))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.isExistUserByEmail)
                .fromLocalToGrpcRequest(context);
    }

    public static UserGrpcService.UserResponse getUserIsExistByEmailResponse() {
        UserResponseContext context = UserResponseContext.builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .isExistState(Optional.of(true))
                .build();
        return UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.isExistUserByEmail)
                .fromLocalToGrpcResponse(context);
    }

}
