package com.hackathonhub.serviceuser.__mocks__;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.__mocks__.mockImpls.UserDeletByIdMocks;
import com.hackathonhub.serviceuser.__mocks__.mockImpls.UserGetByEmailMocks;
import com.hackathonhub.serviceuser.__mocks__.mockImpls.UserIsExistByEmailMocks;
import com.hackathonhub.serviceuser.__mocks__.mockImpls.UserSaveMocks;


public class UserMocksFactory {

    public static UserMockStrategy getMockStrategy (UserGrpcService.actions_enum action) {
        switch (action) {
            case saveUser:
                return new UserSaveMocks();
            case getUserByEmail:
                return new UserGetByEmailMocks();
            case deleteUser:
                return new UserDeletByIdMocks();
            case isExistUserByEmail:
                return new UserIsExistByEmailMocks();
            default:
                throw new UnsupportedOperationException("Mock is not support for this action: " + action);
        }
    }
}
