package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import com.hackathonhub.serviceuser.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserDeleteMapper implements UserMapperStrategy {
    @Override
    public UserGrpcService.UserResponse fromLocalToGrpcResponse(UserResponseContext context) {
        return UserGrpcService.UserResponse
                .newBuilder()
                .setStatus(context.getStatus())
                .setMessage(context.getMessage())
                .build();
    }

    @Override
    public UserGrpcService.UserRequest fromLocalToGrpcRequest(UserRequestContext context) {
        UserGrpcService.UserDeleteByIdRequest request = UserGrpcService.UserDeleteByIdRequest
                .newBuilder()
                .setId(UuidUtils.uuidToString(context
                        .getUserId()
                        .orElseThrow(() -> {
                            log.error("EMAIL_NOT_FOUND_FOR_MAPPING: "
                                    + context.getUserEmail());
                            return new RuntimeException("EMAIL_NOT_FOUND_FOR_MAPPING"
                                    + context.getUserEmail());
                        }
                )))
                .build();
        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.deleteUser)
                .setUserForDelete(request)
                .build();
    }
}