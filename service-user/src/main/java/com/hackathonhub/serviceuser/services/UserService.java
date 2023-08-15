package com.hackathonhub.serviceuser.services;


import com.hackathonhub.serviceuser.grpc.UserGrpc;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.UserMapper;
import com.hackathonhub.serviceuser.mappers.UserMapperGrpcActions;
import com.hackathonhub.serviceuser.mappers.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends UserGrpc.UserImplBase {

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    @Override
    public void saveUser(UserGrpcService.UserSaveRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        User mappedUser = UserMapper.mapGrpcToLocal(request);
        User savedUser = userRepository.save(mappedUser);

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                    .status(UserGrpcService.status_enum.success)
                    .user(Optional.of(savedUser))
                    .message("User succesfuly saved")
                .build();

        responseObserver.onNext(UserMapper.mapLocalToGrpc(UserMapperGrpcActions.save, userResponseContext));

        responseObserver.onCompleted();
    }

    @Override
    public void getUserByEmail(UserGrpcService.UserGetByEmailRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        User user = userRepository.getByEmail(request.getEmail());

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                    .status(UserGrpcService.status_enum.success)
                    .user(Optional.ofNullable(user))
                    .message("User by email founded")
                .build();

        responseObserver
                .onNext(UserMapper.mapLocalToGrpc(UserMapperGrpcActions.getByEmail, userResponseContext));

        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserGrpcService.UserDeleteByIdRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        userRepository.deleteById(UUID.fromString(request.getId()));

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                    .status(UserGrpcService.status_enum.success)
                    .message("User is deleted")
                .build();

        responseObserver.onNext(UserMapper.mapLocalToGrpc(UserMapperGrpcActions.delete, userResponseContext));
        responseObserver.onCompleted();
    }

    @Override
    public void isExistUserByEmail(UserGrpcService.UserGetByEmailRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        Boolean isExist = userRepository.existByEmail(request.getEmail());

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                    .status(UserGrpcService.status_enum.success)
                    .isExist(Optional.ofNullable(isExist))
                    .message("The user exists: " + isExist)
                .build();

        responseObserver.onNext(UserMapper.mapLocalToGrpc(UserMapperGrpcActions.existByEmail, userResponseContext));
        responseObserver.onCompleted();
    }
}
