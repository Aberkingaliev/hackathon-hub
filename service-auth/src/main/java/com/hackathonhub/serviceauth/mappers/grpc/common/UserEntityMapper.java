package com.hackathonhub.serviceauth.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.models.User;

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
        User mappedUser = new User()
                .setId(TypeMapper.toOriginalyUuid(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setRole(RoleMapper.toOriginalyRole(user.getRolesList()));

        if(user.getTeamId().getValue().isEmpty()) {
            return mappedUser;
        }


        return mappedUser.setTeamId(TypeMapper.toOriginalyUuid(user.getTeamId()));
    }
}
