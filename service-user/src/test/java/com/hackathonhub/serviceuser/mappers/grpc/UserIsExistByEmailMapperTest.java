package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.__mocks__.UserMockGrpc;
import com.hackathonhub.serviceuser.mappers.grpc.__mocks__.UserMockLocal;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class UserIsExistByEmailMapperTest {

    private static final String requestedEmailLocal = UserMockLocal.getUserForResponse().getEmail();
    private static final UserGrpcService.UserRequest userIsExistByEmailRequestGrpc = UserMockGrpc.getUserIsExistByEmailRequest();
    private static final UserGrpcService.UserResponse userIsExistByEmailResponseGrpc = UserMockGrpc.getUserIsExistByEmailResponse();



    @Test
    void fromLocalToGrpcResponseTest () {

        UserResponseContext context = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .isExistState(Optional.of(true))
                .message("test")
                .build();

        UserGrpcService.UserResponse mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.isExistUserByEmail)
                .fromLocalToGrpcResponse(context);

        Assertions.assertEquals(userIsExistByEmailResponseGrpc, mappedUser);
    }


    @Test
    void fromLocalToGrpcRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userEmail(Optional.of(requestedEmailLocal))
                .build();

        UserGrpcService.UserRequest mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.isExistUserByEmail)
                .fromLocalToGrpcRequest(context);


        Assertions.assertEquals(userIsExistByEmailRequestGrpc, mappedUser);
    }

}