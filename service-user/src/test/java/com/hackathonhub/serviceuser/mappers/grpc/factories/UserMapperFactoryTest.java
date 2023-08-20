package com.hackathonhub.serviceuser.mappers.grpc.factories;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.UserDeleteMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserGetByEmailMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserIsExistByEmailMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserSaveMapper;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceuser.mappers.grpc.strategies.UserMapperStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Enumeration;

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
    }
}
