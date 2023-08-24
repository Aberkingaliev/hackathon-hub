package com.hackathonhub.serviceauth.__mocks__;


import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.models.User;

public interface UserMockStrategy {

    User getUser (MockLocalUserDataType mockDataType);
    UserGrpcService.UserRequest getRequest ();
    UserGrpcService.UserResponse getResponse ();
}
