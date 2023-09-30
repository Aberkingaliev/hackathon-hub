package com.hackathonhub.serviceuser.services;


import com.hackathonhub.common.grpc.Dto;
import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceuser.constants.ApiUserResponseMessage;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.dtos.UserDto;
import com.hackathonhub.serviceuser.mappers.grpc.common.UserCreateMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.UserDtoMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
@Slf4j
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void createUser(Messages.CreateUserMessage request,
                           StreamObserver<Dto.UserDto> responseObserver) {
        User user = UserCreateMapper.toEntity(request);

        try {
            UserDto savedUser = userRepository.save(user).toDto();

            Dto.UserDto response = UserDtoMapper.toGrpcDto(savedUser);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while creating user: ", e);
            responseObserver
                    .onError(Status.INTERNAL.withDescription("Error while creating user")
                            .asRuntimeException());
        }
    }

    @Override
    public void getUserByEmail(Messages.GetUserByEmailRequest request,
                               StreamObserver<Dto.UserDto> responseObserver) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        user.ifPresentOrElse(u -> {
            Dto.UserDto response = UserDtoMapper.toGrpcDto(u.toDto());

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, () -> responseObserver
                .onError(Status.NOT_FOUND.withDescription(ApiUserResponseMessage.USER_NOT_FOUND)
                        .asRuntimeException()));
    }

    @Override
    public void getUserEntityByEmail(Messages.GetUserByEmailRequest request,
                                     StreamObserver<Entities.User> responseObserver) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        user.ifPresentOrElse(u -> {
            Entities.User response = UserEntityMapper.toGrpcEntity(u);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, () -> responseObserver
                .onError(Status.NOT_FOUND.withDescription(ApiUserResponseMessage.USER_NOT_FOUND)
                        .asRuntimeException()));
    }

    @Override
    public void checkUserExistenceByEmail(Messages.CheckUserExistenceByEmailRequest request,
                                          StreamObserver<Messages.CheckUserExistenceByEmailResponse>
                                                  responseObserver) {
        try {
            Boolean exists = userRepository.existByEmail(request.getEmail());

            Messages.CheckUserExistenceByEmailResponse response =
                    Messages.CheckUserExistenceByEmailResponse
                            .newBuilder()
                            .setIsExist(exists)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while checking user existence: ", e);
            responseObserver
                    .onError(Status.INTERNAL.withDescription("Error while checking user existence")
                            .asRuntimeException());
        }

    }

    public ApiAuthResponse<UserDto> updateUser(UserDto user) {
        ApiAuthResponse<UserDto> responseBuilder = new ApiAuthResponse<>();

        if (user == null || user.getId() == null) {
            return responseBuilder.badRequest(ApiUserResponseMessage.USERID_REQUIRED);
        }

        try {
            Optional<User> foundedUser = userRepository.findById(user.getId());

            return foundedUser.map(u -> {
                        User mappedUser = u.fromDto(user);
                        UserDto updatedUser = userRepository.save(mappedUser).toDto();
                        return responseBuilder.ok(updatedUser, ApiUserResponseMessage.USER_UPDATED);
                    })
                    .orElseGet(() -> responseBuilder.notFound(ApiUserResponseMessage.USER_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while updating user: ", e);
            return responseBuilder.internalServerError(e.getMessage());
        }

    }

}