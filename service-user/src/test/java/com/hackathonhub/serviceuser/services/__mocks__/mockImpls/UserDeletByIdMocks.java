package com.hackathonhub.serviceuser.services.__mocks__.mockImpls;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.services.StaticGrpcResponseMessage;
import com.hackathonhub.serviceuser.services.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.services.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.services.__mocks__.UserMockTestBase;

public class UserDeletByIdMocks extends UserMockTestBase implements UserMockStrategy {

    private static final UserGrpcService.UserRequest request = UserGrpcService.UserRequest
            .newBuilder()
            .setAction(UserGrpcService.actions_enum.deleteUser)
            .setUserForDelete(
                    UserGrpcService.UserDeleteByIdRequest
                    .newBuilder()
                    .setId(userId)
                    .build()
            )
            .build();
    private static final UserGrpcService.UserResponse response = UserGrpcService.UserResponse
            .newBuilder()
            .setStatus(UserGrpcService.status_enum.success)
            .setMessage(StaticGrpcResponseMessage.USER_DELETED)
            .build();


    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        return null;
    }

    @Override
    public UserGrpcService.UserRequest getRequest() {
        return request;
    }

    @Override
    public UserGrpcService.UserResponse getResponse() {
        return response;
    }

}
