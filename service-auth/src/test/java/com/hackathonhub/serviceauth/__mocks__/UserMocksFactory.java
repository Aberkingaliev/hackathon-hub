package com.hackathonhub.serviceauth.__mocks__;

import com.hackathonhub.serviceauth.__mocks__.mockImpls.UserDeletByIdMocks;
import com.hackathonhub.serviceauth.__mocks__.mockImpls.UserGetByEmailMocks;
import com.hackathonhub.serviceauth.__mocks__.mockImpls.UserIsExistByEmailMocks;
import com.hackathonhub.serviceauth.__mocks__.mockImpls.UserSaveMocks;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;

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
