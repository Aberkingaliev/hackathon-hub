package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import com.hackathonhub.serviceauth.mappers.grpc.contexts.UserRequestContext;
import com.hackathonhub.serviceauth.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.models.User;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import com.hackathonhub.serviceauth.grpc.UserGrpc;
import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthService {

    @Autowired
    public AuthService(AuthRepository authRepository, JWTUtils jwtUtils) {
        this.authRepository = authRepository;
        this.jwtUtils = jwtUtils;
    }

    private final AuthRepository authRepository;
    private final JWTUtils jwtUtils;

    public ApiAuthResponse registration(User user) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:55000").usePlaintext().build();
        UserGrpc.UserBlockingStub stub = UserGrpc.newBlockingStub(channel);

        AuthToken savedTokens;
        try {
            boolean isUserExist = checkUserExistenceByEmail(stub, user.getEmail());

            if (isUserExist) {
                return ApiAuthResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("USER_ALREADY_EXIST")
                        .build();
            }

            UserGrpcService.UserResponseData savedUserResponse = saveUser(stub, user);

            HashMap<String, String> generatedTokens = jwtUtils.generateToken(savedUserResponse);

            savedTokens = saveTokens(generatedTokens, UUID.fromString(savedUserResponse.getId()));

            channel.shutdownNow();

            return ApiAuthResponse
                    .builder()
                    .status(HttpStatus.CREATED)
                    .message("USER_SUCCESSFULY REGISTRED")
                    .data(
                            ApiResponseDataContext
                                    .builder()
                                    .data(Optional.of(savedTokens))
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

    private UserGrpcService.UserResponseData saveUser(UserGrpc.UserBlockingStub stub, User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        UserRequestContext userRequestContext = UserRequestContext
                .builder()
                .userData(Optional.of(user.setPassword(hashedPassword)))
                .build();

        UserGrpcService.UserRequest userSaveRequest = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser).fromLocalToGrpcRequest(userRequestContext);

        UserGrpcService.UserResponse savedUserResponse = stub.saveUser(userSaveRequest);

        return savedUserResponse.getUser();
    }

    private AuthToken saveTokens(HashMap<String, String> generatedTokens, UUID userId) {
        AuthToken newToken =
                new AuthToken()
                        .setUserId(userId)
                        .setAccessToken(generatedTokens.get("accessToken"))
                        .setRefreshToken(generatedTokens.get("refreshToken"))
                        .setCreatedAt(System.currentTimeMillis());

        return authRepository.save(newToken);
    }
}
