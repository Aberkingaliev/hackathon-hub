package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.__mocks__.UserMockGrpc;
import com.hackathonhub.serviceuser.mappers.grpc.__mocks__.UserMockLocal;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceuser.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserGetByEmailMapperTest {

    private static final User userByEmailResponseLocal = UserMockLocal.getUserForResponse();
    private static final String requestedEmailLocal = UserMockLocal.getUserForRequest().getEmail();
    private static final UserGrpcService.UserRequest userByEmailRequestGrpc = UserMockGrpc.getUserByEmailRequest();
    private static final UserGrpcService.UserResponse userByEmailResponseGrpc = UserMockGrpc.getUserByEmailResponse();



    @Test
    void fromLocalToGrpcResponseTest () {

        UserResponseContext context = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message("test")
                .userData(Optional.of(userByEmailResponseLocal))
                .build();

        UserGrpcService.UserResponse mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                .fromLocalToGrpcResponse(context);

        Assertions.assertEquals(userByEmailResponseGrpc, mappedUser);
    }


    @Test
    void fromLocalToGrpcRequest() {
        UserRequestContext context = UserRequestContext
                .builder()
                .userEmail(Optional.of(requestedEmailLocal))
                .build();

        UserGrpcService.UserRequest mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                .fromLocalToGrpcRequest(context);


        Assertions.assertEquals(userByEmailRequestGrpc, mappedUser);
    }

    @Test
    void fromGrpcResponseToLocal() {
        UserGrpcService.UserResponse response = UserMockGrpc.getUserByEmailResponse();

        User mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                .fromGrpcResponseToLocal(response);

        Assertions.assertEquals(userByEmailResponseLocal, mappedUser);
    }
}
