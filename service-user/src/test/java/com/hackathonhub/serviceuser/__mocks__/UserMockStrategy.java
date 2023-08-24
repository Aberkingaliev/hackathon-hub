package com.hackathonhub.serviceuser.__mocks__;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;

public interface UserMockStrategy {

    User getUser (MockLocalUserDataType mockDataType);
    UserGrpcService.UserRequest getRequest ();
    UserGrpcService.UserResponse getResponse ();
}
