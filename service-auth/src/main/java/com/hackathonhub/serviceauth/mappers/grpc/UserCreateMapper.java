package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.dtos.UserCreateDto;
import com.hackathonhub.serviceauth.mappers.grpc.common.RoleMapper;
import com.hackathonhub.user_protos.grpc.Messages;

public class UserCreateMapper {

    public static Messages.CreateUserMessage toGrpcDto(UserCreateDto user) {
        return Messages.CreateUserMessage.newBuilder()
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .addAllRoles(RoleMapper.toGrpcEntity(user.getRoles()))
                .build();
    }
}
