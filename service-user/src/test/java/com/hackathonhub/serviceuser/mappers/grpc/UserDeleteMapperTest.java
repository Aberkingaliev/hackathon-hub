package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.services.StaticGrpcResponseMessage;
import com.hackathonhub.serviceuser.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.__mocks__.UserMocksFactory;
import com.hackathonhub.serviceuser.utils.UuidUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;


public class UserDeleteMapperTest {

    private final UserDeleteMapper mapper = new UserDeleteMapper();
    private final UserMockStrategy mockStrategy = UserMocksFactory
            .getMockStrategy(UserGrpcService.actions_enum.deleteUser);


    @Test()
    void fromLocalToGrpcResponse_Test() {
        /*

        GIVEN

         */

        UserResponseContext context= UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message(StaticGrpcResponseMessage.USER_DELETED)
                .build();

        UserGrpcService.UserResponse response = mockStrategy.getResponse();
        /*

        EXECUTE

        */

        UserGrpcService.UserResponse responseFromCallMapper = mapper
                .fromLocalToGrpcResponse(context);

        /*

        ASSERTIONS

         */

        Assertions.assertEquals(response, responseFromCallMapper);

    }

    @Test
    void fromLocalToGrpcRequest_Test() {
        /*

        GIVEN

         */
        String id = mockStrategy
                .getRequest()
                .getUserForDelete()
                .getId();


        UserRequestContext requestContext = UserRequestContext
                .builder()
                .userId(Optional.of(UuidUtils.stringToUUID(id)))
                .build();


        UserGrpcService.UserRequest request = mockStrategy.getRequest();
        /*

        EXECUTE

        */

        UserGrpcService.UserRequest responseFromCallMapper = mapper.fromLocalToGrpcRequest(requestContext);

        /*

        ASSERTIONS

         */

        Assertions.assertEquals(request, responseFromCallMapper);

    }

}
