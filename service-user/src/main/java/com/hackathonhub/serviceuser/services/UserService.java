package com.hackathonhub.serviceuser.services;


import com.hackathonhub.serviceuser.grpc.UserGrpc;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.mappers.grpc.contexts.UserResponseContext;
import com.hackathonhub.serviceuser.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceuser.models.User;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveUser(UserGrpcService.UserRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {

        User mappedUser = UserMapperFactory
                .getMapper(UserGrpcService.actions_enum.saveUser)
                .fromGrpcRequestToLocal(request);

        User savedUser = userRepository.save(mappedUser);

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                    .status(UserGrpcService.status_enum.success)
                    .userData(Optional.of(savedUser))
                    .message("USER_SAVED")
                .build();

        responseObserver.onNext(UserMapperFactory.getMapper(UserGrpcService.actions_enum.saveUser).fromLocalToGrpcResponse(userResponseContext));

        responseObserver.onCompleted();
    }


    @Override
    public void getUserByEmail(UserGrpcService.UserRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        User user = userRepository.getByEmail(request.getUserForGetByEmail().getEmail());

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .userData(Optional.ofNullable(user))
                .message("USER_BY_EMAIL_FOUNDED")
                .build();

        responseObserver
                .onNext(UserMapperFactory.getMapper(UserGrpcService.actions_enum.getUserByEmail).fromLocalToGrpcResponse(userResponseContext));

        responseObserver.onCompleted();
    }


    @Override
    public void deleteUser(UserGrpcService.UserRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        userRepository.deleteById(UUID.fromString(request.getUserForDelete().getId()));

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .message("USER_IS_DELETED")
                .build();

        responseObserver.onNext(UserMapperFactory.getMapper(UserGrpcService.actions_enum.deleteUser).fromLocalToGrpcResponse(userResponseContext));
        responseObserver.onCompleted();
    }

    @Override
    public void isExistUserByEmail(UserGrpcService.UserRequest request, StreamObserver<UserGrpcService.UserResponse> responseObserver) {
        Boolean isExist = userRepository.existByEmail(request.getUserIsExistByEmail().getEmail());

        UserResponseContext userResponseContext = UserResponseContext
                .builder()
                .status(UserGrpcService.status_enum.success)
                .isExistState(Optional.ofNullable(isExist))
                .message("USER_IS_EXIST: " + isExist)
                .build();

        responseObserver.onNext(UserMapperFactory.getMapper(UserGrpcService.actions_enum.isExistUserByEmail).fromLocalToGrpcResponse(userResponseContext));
        responseObserver.onCompleted();
    }
}
