package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.services.StaticGrpcResponseMessage;
import com.hackathonhub.serviceuser.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.__mocks__.UserMocksFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;



public class UserSaveMapperTest {

    private final UserSaveMapper mapper = new UserSaveMapper();
    private final UserMockStrategy mockStrategy = UserMocksFactory
            .getMockStrategy(UserGrpcService.actions_enum.saveUser);


    @Test()
    void fromLocalToGrpcResponse_Test() {
        /*

        GIVEN

         */
        User localUserFromDb = mockStrategy.getUser(MockLocalUserDataType.USER_FROM_DB);

        UserResponseContext context= UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message(StaticGrpcResponseMessage.USER_SAVED)
                .userData(Optional.of(localUserFromDb))
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
    void fromGrpcResponseToLocal_Test() {
        /*

        GIVEN

         */
        User localUserFromDb = mockStrategy.getUser(MockLocalUserDataType.USER_FROM_DB);


        UserGrpcService.UserResponse response = mockStrategy.getResponse();
        /*

        EXECUTE

        */

        User responseFromCallMapper = mapper.fromGrpcResponseToLocal(response);

        /*

        ASSERTIONS

         */

        Assertions.assertEquals(localUserFromDb, responseFromCallMapper);

    }

    @Test
    void fromLocalToGrpcRequest_Test() {
        /*

        GIVEN

         */
        User localUserForRequest = mockStrategy.getUser(MockLocalUserDataType.MAPPED_USER_FROM_REQUEST);

        UserRequestContext requestContext = UserRequestContext
                .builder()
                .userData(Optional.of(localUserForRequest))
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

    @Test
    void fromRequestToLocal_Test() {
        /*

        GIVEN

         */
        User localUserFromRequest = mockStrategy.getUser(MockLocalUserDataType.MAPPED_USER_FROM_REQUEST);


        UserGrpcService.UserRequest request = mockStrategy.getRequest();
        /*

        EXECUTE

        */

        User responseFromCallMapper = mapper.fromGrpcRequestToLocal(request);

        /*

        ASSERTIONS

         */

        Assertions.assertEquals(localUserFromRequest, responseFromCallMapper);

    }
}
