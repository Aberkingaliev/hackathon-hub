package com.hackathonhub.serviceauth.services.security;



import com.hackathonhub.serviceauth.mappers.grpc.factories.UserMapperFactory;
import com.hackathonhub.serviceuser.grpc.UserGrpc;
import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:55000").usePlaintext().build();
        UserGrpc.UserBlockingStub stub = UserGrpc.newBlockingStub(channel);

        UserGrpcService.UserRequest request = UserGrpcService.UserRequest
                .newBuilder()
                .setUserForGetByEmail(UserGrpcService.UserGetByEmailRequest
                        .newBuilder()
                        .setEmail(email)
                        .build())
                .setAction(UserGrpcService.actions_enum.getUserByEmail)
                .build();

        UserGrpcService.UserResponse response = stub.getUserByEmail(request);

        if (!response.hasUser()) {
            throw new UsernameNotFoundException("User " + email + " not found");
        }

        return UserDetailsImpl
                .build(
                        UserMapperFactory
                        .getMapper(UserGrpcService.actions_enum.getUserByEmail)
                        .fromGrpcResponseToLocal(response)
                );
    }
}
