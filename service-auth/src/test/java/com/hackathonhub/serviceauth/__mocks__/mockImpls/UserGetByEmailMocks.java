package com.hackathonhub.serviceauth.__mocks__.mockImpls;

import com.hackathonhub.serviceauth.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceauth.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceauth.__mocks__.UserMockTestBase;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.services.StaticGrpcResponseMessage;

public class UserGetByEmailMocks extends UserMockTestBase implements UserMockStrategy {


    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        User user = new User()
                .setId(userUuid)
                .setUsername(username)
                .setFullName(fullName)
                .setEmail(email)
                .setPassword(password)
                .setTeamId(teamUuid)
                .setActivated(isActive)
                .setRole(HashSetRole);
        return switch (mockDataType) {
            case MAPPED_USER_FROM_REQUEST -> new User().from(user.setId(null));
            case USER_FROM_DB, MAPPED_USER_FROM_RESPONSE -> new User().from(user);
        };
    }

    @Override
    public UserGrpcService.UserRequest getRequest() {
        return UserGrpcService.UserRequest
                .newBuilder()
                .setAction(UserGrpcService.actions_enum.getUserByEmail)
                .setUserForGetByEmail(UserGrpcService
                        .UserGetByEmailRequest
                        .newBuilder()
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
                .setMessage(StaticGrpcResponseMessage.USER_BY_EMAIL_FOUNDED)
                .setUser(
                        UserGrpcService.UserResponseData
                                .newBuilder()
                                .setId(userId)
                                .setUsername(username)
                                .setFullName(fullName)
                                .setEmail(email)
                                .setPassword(password)
                                .setTeamId(teamId)
                                .setIsActivated(isActive)
                                .addAllRoles(listRole)
                                .build()
                )
                .build();
    }

}

