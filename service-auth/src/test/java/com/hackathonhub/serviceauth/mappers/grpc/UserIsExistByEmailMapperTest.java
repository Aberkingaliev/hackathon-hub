package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceauth.__mocks__.UserMocksFactory;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserIsExistByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.user.contexts.UserResponseContext;
import com.hackathonhub.serviceauth.constants.GrpcResponseMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;


public class UserIsExistByEmailMapperTest {

    private final UserIsExistByEmailMapper mapper = new UserIsExistByEmailMapper();
    private final UserMockStrategy mockStrategy = UserMocksFactory
            .getMockStrategy(UserGrpcService.actions_enum.isExistUserByEmail);


    @Test()
    void fromLocalToGrpcResponse_Test() {
        /*

        GIVEN

         */

        UserResponseContext context= UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message(GrpcResponseMessage.USER_IS_EXIST)
                .isExistState(Optional.of(true))
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
        String email = mockStrategy
                .getRequest()
                .getUserIsExistByEmail()
                .getEmail();


        UserRequestContext requestContext = UserRequestContext
                .builder()
                .userEmail(Optional.of(email))
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
