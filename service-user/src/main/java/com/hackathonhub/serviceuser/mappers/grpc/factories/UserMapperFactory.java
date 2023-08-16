package com.hackathonhub.serviceuser.mappers.grpc.factories;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.UserDeleteMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserGetByEmailMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserIsExistByEmailMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserSaveMapper;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;

public class UserMapperFactory {
    public static UserMapperStrategy getMapper(UserGrpcService.actions_enum action) {
        UserMapperStrategy mapper = null;

        switch (action) {
            case saveUser -> mapper = new UserSaveMapper();
            case deleteUser -> mapper = new UserDeleteMapper();
            case getUserByEmail -> mapper = new UserGetByEmailMapper();
            case isExistUserByEmail -> mapper = new UserIsExistByEmailMapper();
            default -> throw new UnsupportedOperationException("Mapper is not support for this action: " + action);
        }

        return mapper;
    }
}
