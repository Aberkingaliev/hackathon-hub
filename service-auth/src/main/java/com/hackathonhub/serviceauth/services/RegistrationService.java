package com.hackathonhub.serviceauth.services;

import com.hackathonhub.common.grpc.Dto;
import com.hackathonhub.serviceauth.constants.ApiAuthResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserCreateDto;
import com.hackathonhub.serviceauth.dtos.UserDto;
import com.hackathonhub.serviceauth.mappers.grpc.UserCreateMapper;
import com.hackathonhub.serviceauth.mappers.grpc.common.UserDtoMapper;
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

    public ApiAuthResponse<UserDto> registration (UserCreateDto user) {
        ApiAuthResponse.ApiAuthResponseBuilder<UserDto> responseBuilder =  ApiAuthResponse
                .builder();
        try {
            if (checkUserExistenceByEmail(user.getEmail())) {
                return responseBuilder
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ApiAuthResponseMessage.USER_ALREADY_REGISTERED)
                        .build();
            }

            Dto.UserDto savedUser = createUser(user);
            UserDto mappedUser = UserDtoMapper.toOriginalDto(savedUser);

            return responseBuilder
                    .status(HttpStatus.CREATED)
                    .message(ApiAuthResponseMessage.USER_SUCCESS_REGISTERED)
                    .data(mappedUser)
                    .build();
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(ApiAuthResponseMessage.registrationFailed(e.getMessage()))
                    .build();
        }

    }

    private Dto.UserDto createUser(UserCreateDto user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        Messages.CreateUserMessage request = UserCreateMapper.toGrpcDto(user);

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
