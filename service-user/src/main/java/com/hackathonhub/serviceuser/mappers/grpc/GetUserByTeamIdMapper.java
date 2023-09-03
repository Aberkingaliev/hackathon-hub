package com.hackathonhub.serviceuser.mappers.grpc;

import com.hackathonhub.serviceuser.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.user_protos.grpc.Messages;

import java.util.Set;
import java.util.stream.Collectors;

public class GetUserByTeamIdMapper {

    public static Messages.GetUsersByTeamIdResponse toGrpcEntity (Set<User> users) {
        return Messages.GetUsersByTeamIdResponse.newBuilder()
                .addAllUsers(users.stream()
                        .map(UserEntityMapper::toGrpcEntity)
                        .collect(Collectors.toSet()))
                .build();
    }
}
