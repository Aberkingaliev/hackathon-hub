package com.hackathonhub.serviceteam.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceteam.models.User;

public class UserEntityMapper {

    public static Entities.User toGrpcEntity (User user) {
        Entities.User.Builder userBuilder = Entities.User.newBuilder()
                .setId(TypeMapper.toGrpcUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .addAllRoles(RoleMapper.toGrpcEntity(user.getRoles()));


        if (user.getTeamId() == null) {
            return userBuilder.build();
        }

        return userBuilder.setTeamId(TypeMapper.toGrpcUuid(user.getTeamId())).build();
    }

    public static User toEntity (Entities.User user) {
        return new User()
                .setId(TypeMapper.toOriginalyUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setTeamId(TypeMapper.toOriginalyUuid(user.getTeamId()))
                .setActivated(user.getIsActivated())
                .setRole(RoleMapper.toOriginalyRole(user.getRolesList()));
    }
}
