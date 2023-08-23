package com.hackathonhub.serviceuser.services.__mocks__.mockImpls;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.services.StaticGrpcResponseMessage;
import com.hackathonhub.serviceuser.services.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.services.__mocks__.UserMockStrategy;
import com.hackathonhub.serviceuser.services.__mocks__.UserMockTestBase;

public class UserSaveMocks extends UserMockTestBase implements UserMockStrategy {

    private static final User localUser = new User()
            .setId(userUuid)
            .setUsername(username)
            .setFullName(fullName)
            .setEmail(email)
            .setPassword(password)
            .setTeamId(teamUuid)
            .setActivated(isActive)
            .setRole(HashSetRole);;
    private static final UserGrpcService.UserRequest request = UserGrpcService.UserRequest
            .newBuilder()
            .setAction(UserGrpcService.actions_enum.saveUser)
            .setUserForSave(UserGrpcService.UserSaveRequest
                    .newBuilder()
                    .setUsername(username)
                    .setFullName(fullName)
                    .setEmail(email)
                    .setPassword(password)
                    .setTeamId(teamId)
                    .setIsActivated(isActive)
                    .addAllRoles(listRole)
                    .build())
            .build();
    private static final UserGrpcService.UserResponse response = UserGrpcService.UserResponse
            .newBuilder()
            .setStatus(UserGrpcService.status_enum.success)
            .setMessage(StaticGrpcResponseMessage.USER_SAVED)
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

    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        return switch (mockDataType) {
            case MAPPED_USER_FROM_REQUEST -> localUser.setId(null);
            case USER_FROM_DB, MAPPED_USER_FROM_RESPONSE -> localUser;
        };
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

