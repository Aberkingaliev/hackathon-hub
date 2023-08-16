package com.hackathonhub.serviceuser.mappers.grpc.contexts;

import com.hackathonhub.serviceuser.models.User;
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
