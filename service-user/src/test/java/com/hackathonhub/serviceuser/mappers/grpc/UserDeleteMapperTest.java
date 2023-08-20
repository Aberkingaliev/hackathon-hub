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

public class UserDeleteMapperTest {

    private static final UUID requestedIdLocal = UserMockLocal.getUserForResponse().getId();
    private static final UserGrpcService.UserRequest userDeleteRequestGrpc = UserMockGrpc.getUserDeleteRequest();
    private static final UserGrpcService.UserResponse userDeleteResponseGrpc = UserMockGrpc.getUserDeleteResponse();



    @Test
    void fromLocalToGrpcResponseTest () {

        UserResponseContext context = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .build();

        UserGrpcService.UserResponse mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.deleteUser)
                .fromLocalToGrpcResponse(context);

        Assertions.assertEquals(userDeleteResponseGrpc, mappedUser);
    }


    @Test
    void fromLocalToGrpcRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userId(Optional.of(requestedIdLocal))
                .build();

        UserGrpcService.UserRequest mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.deleteUser)
                .fromLocalToGrpcRequest(context);


        Assertions.assertEquals(userDeleteRequestGrpc, mappedUser);
    }

}
