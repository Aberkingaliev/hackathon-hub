package com.hackathonhub.serviceuser.services;


import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceuser.mappers.grpc.GetUserByTeamIdMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.TypeMapper;
import com.hackathonhub.serviceuser.mappers.grpc.UserCreateMapper;
import com.hackathonhub.serviceuser.mappers.grpc.common.UserEntityMapper;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import com.hackathonhub.user_protos.grpc.Messages;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

@GrpcService
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

}