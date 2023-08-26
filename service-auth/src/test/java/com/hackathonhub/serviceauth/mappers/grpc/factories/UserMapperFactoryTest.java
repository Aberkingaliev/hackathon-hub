package com.hackathonhub.serviceauth.mappers.grpc.factories;

import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import com.hackathonhub.serviceauth.mappers.grpc.UserDeleteMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserGetByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserIsExistByEmailMapper;
import com.hackathonhub.serviceauth.mappers.grpc.UserSaveMapper;
import com.hackathonhub.serviceauth.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.mappers.grpc.strategies.UserMapperStrategy;
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
