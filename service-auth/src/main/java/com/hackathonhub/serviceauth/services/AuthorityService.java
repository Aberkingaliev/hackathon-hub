package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.constants.ApiRolePermission;
import com.hackathonhub.serviceauth.grpc.AuthorityGrpc;
import com.hackathonhub.serviceauth.grpc.AuthorityGrpcService;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@GrpcService
public class AuthorityService extends AuthorityGrpc.AuthorityImplBase {
    @Autowired
    JWTUtils jwtUtils;

    @Override
    public void authorityAccessCheck(AuthorityGrpcService.AuthorityAccessRequest request,
                                     StreamObserver<AuthorityGrpcService.AuthorityAccessResponse> responseObserver) {
        String parsedAccessToken = request.getAccessToken();
        Set<RoleEnum> roles = jwtUtils.getRolesFromToken(parsedAccessToken);

        boolean authorityIsAccessed = ApiRolePermission.isRoleAllowed(request.getRoute(), roles);

        AuthorityGrpcService.AuthorityAccessResponse response =
                AuthorityGrpcService.AuthorityAccessResponse
                        .newBuilder()
                        .setHasAccess(authorityIsAccessed)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
