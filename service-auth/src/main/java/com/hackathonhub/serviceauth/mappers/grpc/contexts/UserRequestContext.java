package com.hackathonhub.serviceauth.mappers.grpc.contexts;

import com.hackathonhub.serviceauth.models.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;


@Builder
@Getter
public class UserRequestContext {

        private Optional<User> userData;
        private Optional<UUID> userId;
        private Optional<String> userEmail;
}
