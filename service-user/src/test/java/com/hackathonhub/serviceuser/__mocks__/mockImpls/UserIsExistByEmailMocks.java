package com.hackathonhub.serviceuser.__mocks__.mockImpls;

import com.hackathonhub.serviceuser.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.__mocks__.UserMockTestBase;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.services.StaticGrpcResponseMessage;

public class UserIsExistByEmailMocks extends UserMockTestBase implements UserMockStrategy {


    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        return null;
    }

    @Override
    public UserGrpcService.UserRequest getRequest() {
        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.isExistUserByEmail)
                .setUserIsExistByEmail(
                        UserGrpcService.UserIsExistByEmailRequest.
                                newBuilder()
                                .setEmail(email)
                                .build()
                )
                .build();
    }

    @Override
    public UserGrpcService.UserResponse getResponse() {
        return UserGrpcService.UserResponse
                .newBuilder()
                .setStatus(UserGrpcService.status_enum.success)
                .setMessage(StaticGrpcResponseMessage.USER_IS_EXIST)
                .setIsUserAlreadyExist(true)
                .build();
    }

}

