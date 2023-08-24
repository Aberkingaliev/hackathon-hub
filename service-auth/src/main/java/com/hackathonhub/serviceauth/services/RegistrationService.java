package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.grpc.UserGrpc;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class RegistrationService {

    public ApiAuthResponse registration(User user) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:55000").usePlaintext().build();
        UserGrpc.UserBlockingStub stub = UserGrpc.newBlockingStub(channel);

        try {
            boolean isUserExist = checkUserExistenceByEmail(stub, user.getEmail());

            if (isUserExist) {
                return ApiAuthResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("USER_ALREADY_EXIST")
                        .build();
            }

            UserGrpcService.UserResponse savedUserResponse = saveUser(stub, user);


            channel.shutdownNow();

            return ApiAuthResponse
                    .builder()
                    .status(HttpStatus.CREATED)
                    .message("USER_SUCCESSFULY_REGISTRED")
                    .data(ApiResponseDataContext
                            .builder()
                            .user(Optional.of(UserMapperFactory
                                    .getMapper(UserGrpcService.actions_enum.saveUser)
                                    .fromGrpcResponseToLocal(savedUserResponse)))
                            .build())
                    .build();

        } catch (StatusRuntimeException e) {

            channel.shutdownNow();

            return ApiAuthResponse
                    .builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("REGISTRATION_FAILED: " + e.getMessage())
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
