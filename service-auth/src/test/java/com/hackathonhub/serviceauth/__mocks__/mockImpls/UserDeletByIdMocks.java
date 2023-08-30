package com.hackathonhub.serviceauth.__mocks__.mockImpls;


import com.hackathonhub.serviceauth.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceauth.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceauth.__mocks__.UserMockTestBase;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.constants.GrpcResponseMessage;

public class UserDeletByIdMocks extends UserMockTestBase implements UserMockStrategy {


    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        return null;
    }

    @Override
    public UserGrpcService.UserRequest getRequest() {
        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.deleteUser)
                .setUserForDelete(
                        UserGrpcService.UserDeleteByIdRequest
                                .newBuilder()
                                .setId(userId)
                                .build()
                )
                .build();
    }

    @Override
    public UserGrpcService.UserResponse getResponse() {
        return UserGrpcService.UserResponse
                .newBuilder()
                .setStatus(UserGrpcService.status_enum.success)
                .setMessage(GrpcResponseMessage.USER_DELETED)
                .build();
    }

}

