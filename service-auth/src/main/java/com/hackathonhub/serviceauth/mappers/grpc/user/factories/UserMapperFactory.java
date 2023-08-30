package com.hackathonhub.serviceauth.mappers.grpc.user.factories;

import com.hackathonhub.serviceauth.mappers.grpc.user.UserDeleteMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserGetByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserIsExistByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserSaveMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.strategies.UserMapperStrategy;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;

public class UserMapperFactory {

    private static final String NOT_SUPPORTED_EXCEPTION = "NOT_SUPPORTED ACTION: ";

    public static UserMapperStrategy getMapper(UserGrpcService.actions_enum action) {
        UserMapperStrategy mapper = null;

        switch (action) {
            case saveUser -> mapper = new UserSaveMapper();
            case deleteUser -> mapper = new UserDeleteMapper();
            case getUserByEmail -> mapper = new UserGetByEmailMapper();
            case isExistUserByEmail -> mapper = new UserIsExistByEmailMapper();
            default -> throw new UnsupportedOperationException(
                    NOT_SUPPORTED_EXCEPTION + action);
        }

        return mapper;
    }
}
