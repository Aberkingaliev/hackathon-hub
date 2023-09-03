package com.hackathonhub.serviceauth.mappers.grpc;

import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.user_protos.grpc.Messages;

public class UserCreateMapper {

    public static Messages.CreateUserRequest toGrpcCreateDto(User user) {
        return Messages.CreateUserRequest.newBuilder()
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .build();
    }

}
