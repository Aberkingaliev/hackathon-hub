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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RegistrationService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public ApiAuthResponse<UserDto> registration (UserCreateDto user) {
        ApiAuthResponse<UserDto> responseBuilder = new ApiAuthResponse<>();
        try {
            if (checkUserExistenceByEmail(user.getEmail())) {
                return responseBuilder.conflict(ApiAuthResponseMessage.USER_ALREADY_REGISTERED);
            }

            Dto.UserDto savedUser = createUser(user);
            UserDto mappedUser = UserDtoMapper.toOriginalDto(savedUser);

            return responseBuilder.created(mappedUser, ApiAuthResponseMessage.USER_SUCCESS_REGISTERED);
        } catch (Exception e) {
            log.error("Registration failed: ", e);
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
    private boolean checkUserExistenceByEmail (String email) {
        Messages.CheckUserExistenceByEmailRequest request =
                Messages.CheckUserExistenceByEmailRequest.newBuilder()
                        .setEmail(email)
                        .build();

        return userStub.checkUserExistenceByEmail(request).getIsExist();
    }
    private Dto.UserDto createUser(UserCreateDto user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        Messages.CreateUserMessage request = UserCreateMapper.toGrpcDto(user);

        return userStub.createUser(request);
    }
}
