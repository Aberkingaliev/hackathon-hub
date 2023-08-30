package com.hackathonhub.serviceuser.mappers.grpc.contexts;

import com.hackathonhub.serviceuser.models.User;
import lombok.Builder;
import lombok.Getter;
import java.util.Optional;
import java.util.UUID;


@Builder
@Getter
public class UserRequestContext {

        public Optional<User> getUserData() {
                return userData;
        }

        public Optional<UUID> getUserId() {
                return userId;
        }

        public Optional<String> getUserEmail() {
                return userEmail;
        }

        private Optional<User> userData;
        private Optional<UUID> userId;
        private Optional<String> userEmail;
}
