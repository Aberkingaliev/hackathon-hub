package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.user_protos.grpc.Messages;

public class UserCreateMapper {

    public static User toCreateDto(Messages.CreateUserRequest user) {
        return new User()
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated());
    }

}
