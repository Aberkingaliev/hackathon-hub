package com.hackathonhub.serviceauth.services;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.constants.AuthApiResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.mappers.grpc.UserCreateMapper;
import com.hackathonhub.serviceauth.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public ApiAuthResponse<User> registration (User user) {
        ApiAuthResponse.ApiAuthResponseBuilder<User> responseBuilder =  ApiAuthResponse
                .builder();
        try {
            boolean isUserExist = checkUserExistenceByEmail(user.getEmail());

            if (isUserExist) {
                return responseBuilder
                        .status(HttpStatus.BAD_REQUEST)
                        .message(AuthApiResponseMessage.USER_ALREADY_REGISTRED)
                        .build();
            }

            Entities.User savedUser = createUser(user);
            User mappedUser = UserEntityMapper.toEntity(savedUser);

            return responseBuilder
                    .status(HttpStatus.CREATED)
                    .message(AuthApiResponseMessage.USER_SUCCESS_REGISTERED)
                    .data(mappedUser)
                    .build();
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(AuthApiResponseMessage.registrationFailed(e.getMessage()))
                    .build();
        }

    }

    private Entities.User createUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        Messages.CreateUserRequest request = UserCreateMapper.toGrpcCreateDto(user);

        return userStub.createUser(request);
    }

    private boolean checkUserExistenceByEmail (String email) {
        Messages.CheckUserExistenceByEmailRequest request =
                Messages.CheckUserExistenceByEmailRequest.newBuilder()
                        .setEmail(email)
                        .build();

        return userStub.checkUserExistenceByEmail(request).getIsExist();
    }
}
