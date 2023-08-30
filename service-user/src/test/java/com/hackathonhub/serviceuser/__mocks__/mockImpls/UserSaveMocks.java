package com.hackathonhub.serviceuser.__mocks__.mockImpls;

import com.hackathonhub.serviceuser.__mocks__.UserMockTestBase;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.constants.GrpcResponseMessage;
import com.hackathonhub.serviceuser.__mocks__.MockLocalUserDataType;
import com.hackathonhub.serviceuser.__mocks__.UserMockStrategy;

public class UserSaveMocks extends UserMockTestBase implements UserMockStrategy {


    @Override
    public User getUser(MockLocalUserDataType mockDataType) {
        User user = new User()
                .setId(null)
                .setUsername(username)
                .setFullName(fullName)
                .setEmail(email)
                .setPassword(password)
                .setTeamId(teamUuid)
                .setActivated(isActive)
                .setRole(HashSetRole);
        return switch (mockDataType) {
            case MAPPED_USER_FROM_REQUEST -> new User().from(user);
            case USER_FROM_DB, MAPPED_USER_FROM_RESPONSE -> new User().from(user.setId(userUuid));
        };
    }

    @Override
    public UserGrpcService.UserRequest getRequest() {
        return UserGrpcService.UserRequest
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
    }

    @Override
    public UserGrpcService.UserResponse getResponse() {
        return
                UserGrpcService.UserResponse
                        .newBuilder()
                        .setStatus(UserGrpcService.status_enum.success)
                        .setMessage(GrpcResponseMessage.USER_SAVED)
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

