package com.hackathonhub.serviceuser.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceuser.models.User;

import java.util.Optional;

public class UserEntityMapper {

    public static Entities.User toGrpcEntity (User user) {
        return Entities.User.newBuilder()
                .setId(TypeMapper.toGrpcUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .addAllRoles(RoleMapper.toGrpcEntity(user.getRoles()))
                .build();
    }

    public static User toEntity (Entities.User user) {
        return new User()
                .setId(TypeMapper.toOriginalyUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setRole(RoleMapper.toOriginalyRole(user.getRolesList()));
    }
}
