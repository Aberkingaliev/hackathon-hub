package com.hackathonhub.serviceuser.services;


import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.mappers.grpc.GetUserByTeamIdMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.TypeMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserCreateMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.UUID;

@GrpcService
@Slf4j
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void createUser(Messages.CreateUserRequest request,
                           StreamObserver<Entities.User> responseObserver) {
        User user = UserCreateMapper.toCreateDto(request);

        User savedUser = userRepository.save(user);

        Entities.User response = UserEntityMapper
                .toGrpcEntity(savedUser);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByEmail(Messages.GetUserByEmailRequest request,
                               StreamObserver<Entities.User> responseObserver) {
        String email = request.getEmail();

        User user = userRepository.getByEmail(email);

        Entities.User response = UserEntityMapper
                .toGrpcEntity(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersByTeamId(Messages.GetUsersByTeamIdRequest request,
                                 StreamObserver<Messages.GetUsersByTeamIdResponse> responseObserver) {
        UUID teamId = TypeMapper.toOriginalyUuid(request.getTeamId());

        Set<User> users = userRepository.getUsersByTeamId(teamId);

        Messages.GetUsersByTeamIdResponse response = GetUserByTeamIdMapper.toGrpcEntity(users);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkUserExistenceByEmail(Messages.CheckUserExistenceByEmailRequest request,
                                          StreamObserver<Messages.CheckUserExistenceByEmailResponse> responseObserver) {
        String email = request.getEmail();

        Boolean exists = userRepository.existByEmail(email);

        Messages.CheckUserExistenceByEmailResponse response =
                Messages.CheckUserExistenceByEmailResponse
                .newBuilder()
                .setIsExist(exists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public ApiAuthResponse<User> updateUser(User user) {
        ApiAuthResponse.ApiAuthResponseBuilder<User> responseBuilder = ApiAuthResponse.builder();

        try {
            User foundedUser = userRepository.getByEmail(user.getEmail());

            if(foundedUser == null) {
                return responseBuilder
                        .status(HttpStatus.NOT_FOUND)
                        .message("User not found")
                        .build();
            };

            User updatedUser = userRepository.save(foundedUser.from(user));

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message("User updated successfully")
                    .data(updatedUser)
                    .build();

        } catch (Exception e) {
            log.error("Error while updating user: ", e);
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while updating user")
                    .build();
        }

    }

}