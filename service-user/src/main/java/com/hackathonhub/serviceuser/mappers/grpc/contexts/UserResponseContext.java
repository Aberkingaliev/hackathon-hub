package com.hackathonhub.serviceuser.mappers.grpc.contexts;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;


@Builder
@Getter
public class UserResponseContext {

        public Optional<User> getUserData() {
                return userData;
        }

        public Optional<Boolean> getIsExistState() {
                return isExistState;
        }

        public UserGrpcService.status_enum getStatus() {
                return status;
        }

        public String getMessage() {
                return message;
        }

        private Optional<User> userData;
        private Optional<Boolean> isExistState;
        private UserGrpcService.status_enum status;
        private String message;


}
