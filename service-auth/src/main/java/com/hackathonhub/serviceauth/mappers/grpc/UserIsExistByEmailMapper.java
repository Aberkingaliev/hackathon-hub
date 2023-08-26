package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                .setEmail(
                        context
                                .getUserEmail()
                                .orElseThrow(
                                        () -> {
                                            log.error("EMAIL_NOT_FOUND_FOR_MAPPING: " + context.getUserEmail());
                                            return new RuntimeException("EMAIL_NOT_FOUND_FOR_MAPPING" + context.getUserEmail());
                                        }
                                )
                )
                .build();

        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.isExistUserByEmail)
                .setUserIsExistByEmail(request)
                .build();
    }

}
