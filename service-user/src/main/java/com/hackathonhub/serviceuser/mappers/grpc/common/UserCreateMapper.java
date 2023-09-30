package com.hackathonhub.serviceuser.mappers.grpc.common;

import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.user_protos.grpc.Messages;

public class UserCreateMapper {

    public static User toEntity(Messages.CreateUserMessage user) {
        return new User()
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .setRoles(RoleMapper.toOriginalyRole(user.getRolesList()));
    }
}
