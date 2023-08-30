package com.hackathonhub.serviceauth.mappers.grpc.user.factories;

import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserDeleteMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserGetByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserIsExistByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.UserSaveMapper;
import com.hackathonhub.serviceauth.mappers.grpc.user.strategies.UserMapperStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperFactoryTest {

    @Test
    void getMapper() {
        UserMapperStrategy saveUserMapper = UserMapperFactory.getMapper(UserGrpcService.actions_enum.saveUser);
        UserMapperStrategy getByEmailMapper = UserMapperFactory.getMapper(UserGrpcService.actions_enum.getUserByEmail);
        UserMapperStrategy deleteMapper = UserMapperFactory.getMapper(UserGrpcService.actions_enum.deleteUser);
        UserMapperStrategy isExistByEmailMapper = UserMapperFactory.getMapper(UserGrpcService.actions_enum.isExistUserByEmail);

        Assertions.assertEquals(UserSaveMapper.class , saveUserMapper.getClass());
        Assertions.assertEquals(UserGetByEmailMapper.class , getByEmailMapper.getClass());
        Assertions.assertEquals(UserDeleteMapper.class , deleteMapper.getClass());
        Assertions.assertEquals(UserIsExistByEmailMapper.class , isExistByEmailMapper.getClass());
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> UserMapperFactory.getMapper(UserGrpcService.actions_enum.UNRECOGNIZED),
                "Mapper is not support for this action: UNRECOGNIZED"
        );
    }
}
