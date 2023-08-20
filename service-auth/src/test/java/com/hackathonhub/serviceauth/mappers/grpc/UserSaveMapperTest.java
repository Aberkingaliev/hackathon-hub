package com.hackathonhub.serviceauth.mappers.grpc;


import com.hackathonhub.serviceauth.mappers.grpc.__mocks__.UserMockGrpc;
import com.hackathonhub.serviceauth.mappers.grpc.__mocks__.UserMockLocal;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserSaveMapperTest {

    private static final User userSaveResponseLocal = UserMockLocal.getUserForResponse();
    private static final User userSaveRequestLocal = UserMockLocal.getUserForRequest();
    private static final UserGrpcService.UserRequest userSaveRequestGrpc = UserMockGrpc.getUserSaveRequest();
    private static final UserGrpcService.UserResponse userSaveResponseGrpc = UserMockGrpc.getUserForResponse();



    @Test
    void fromLocalToGrpcResponseTest () {

        UserResponseContext context = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .userData(Optional.of(userSaveResponseLocal))
                .build();

        UserGrpcService.UserResponse mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromLocalToGrpcResponse(context);

        Assertions.assertEquals(userSaveResponseGrpc, mappedUser);
    }


    @Test
    void fromLocalToGrpcRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userData(Optional.of(userSaveRequestLocal))
                .build();

        UserGrpcService.UserRequest mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromLocalToGrpcRequest(context);


        Assertions.assertEquals(userSaveRequestGrpc, mappedUser);
    }

    @Test
    void fromGrpcRequestToLocal() {
        User mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromGrpcRequestToLocal(userSaveRequestGrpc);

        Assertions.assertEquals(userSaveRequestLocal, mappedUser);
    }

    @Test
    void fromGrpcResponseToLocal() {
        User mappedUser = UserMapperFactory.getMapper(UserGrpcService.actions_enum.saveUser).fromGrpcResponseToLocal(userSaveResponseGrpc);

        Assertions.assertEquals(userSaveResponseLocal, mappedUser);
    }
}
