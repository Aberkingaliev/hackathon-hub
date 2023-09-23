package com.hackathonhub.serviceteam.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceteam.models.User;

import java.util.HashSet;

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
                .setId(TypeMapper.toOriginallyUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setRole(new HashSet<>(RoleMapper.toOriginallyRole(user.getRolesList())));
    }
}
