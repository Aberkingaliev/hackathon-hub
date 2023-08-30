package com.hackathonhub.serviceauth.mappers.grpc.user.contexts;

import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;


@Builder
@Getter
public class UserResponseContext {

        private final Optional<User> userData;
        private final Optional<Boolean> isExistState;
        private final UserGrpcService.status_enum status;
        private final String message;

}
