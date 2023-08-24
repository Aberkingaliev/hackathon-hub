package com.hackathonhub.serviceauth.mappers.grpc.factories;

import com.hackathonhub.serviceauth.mappers.grpc.UserDeleteMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserGetByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserIsExistByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserSaveMapper;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
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
