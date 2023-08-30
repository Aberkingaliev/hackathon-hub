package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.constants.AuthApiResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import com.hackathonhub.serviceauth.mappers.grpc.user.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.user.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.grpc.UserGrpc;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Slf4j
@Service
public class RegistrationService {

    @GrpcClient("service-user")
    private UserGrpc.UserBlockingStub userStub;

    public ApiAuthResponse registration(User user) {

        try {
            boolean isUserExist = checkUserExistenceByEmail(userStub, user.getEmail());

            if (isUserExist) {
                return ApiAuthResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(AuthApiResponseMessage.USER_ALREADY_REGISTRED)
                        .build();
            }

            UserGrpcService.UserResponse savedUserResponse = saveUser(userStub, user);

            return ApiAuthResponse
                    .builder()
                    .status(HttpStatus.CREATED)
                    .message(AuthApiResponseMessage.USER_SUCCESS_REGISTERED)
                    .data(ApiResponseDataContext
                            .builder()
                            .user(Optional.of(UserMapperFactory
                                    .getMapper(UserGrpcService.actions_enum.saveUser)
                                    .fromGrpcResponseToLocal(savedUserResponse)))
                            .build())
                    .build();

        } catch (StatusRuntimeException e) {

            log.error("Registration failed: {}", e.getMessage());
            return ApiAuthResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(AuthApiResponseMessage.registrationFailed(e.getMessage()))
                    .build();
        }


    }

    private boolean checkUserExistenceByEmail(UserGrpc.UserBlockingStub stub, String email) {
        UserGrpcService.UserRequest isExistByEmailRequest = UserGrpcService.UserRequest
                .newBuilder()
                .setUserIsExistByEmail(
                        UserGrpcService.UserIsExistByEmailRequest
                                .newBuilder()
                                .setEmail(email)
                                .build()
                )
                .setAction(UserGrpcService.actions_enum.isExistUserByEmail)
                .build();

        return stub
                .isExistUserByEmail(isExistByEmailRequest)
                .getIsUserAlreadyExist();
    }

    private UserGrpcService.UserResponse saveUser(UserGrpc.UserBlockingStub stub, User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        UserRequestContext userRequestContext = UserRequestContext
                .builder()
                .userData(Optional.of(user.setPassword(hashedPassword)))
                .build();

        UserGrpcService.UserRequest userSaveRequest = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser).fromLocalToGrpcRequest(userRequestContext);

        return stub.saveUser(userSaveRequest);
    }

}
