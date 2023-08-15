package com.hackathonhub.serviceuser.mappers.contexts;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;


@Builder
@Getter
public class UserResponseContext {

        private final Optional<User> user;
        private final Optional<Boolean> isExist;
        private final UserGrpcService.status_enum status;
        private final String message;

}
