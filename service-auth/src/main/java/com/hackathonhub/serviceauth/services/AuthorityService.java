package com.hackathonhub.serviceauth.services;

import com.hackathonhub.auth_protos.grpc.AuthorityServiceGrpc;
import com.hackathonhub.auth_protos.grpc.Messages;
import com.hackathonhub.serviceauth.constants.ApiRolePermission;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class AuthorityService extends AuthorityServiceGrpc.AuthorityServiceImplBase {
    @Autowired
    JWTUtils jwtUtils;

    @Override
    public void checkAuthority(Messages.CheckAuthorityRequest request,
                               StreamObserver<Messages.CheckAuthorityResponse> responseObserver) {
        Set<RoleEnum> roles = jwtUtils.getRolesFromToken(request.getAccessToken());
        boolean hasAccess = ApiRolePermission.isRoleAllowed(request.getRoute(), roles);

        Messages.CheckAuthorityResponse response = Messages.CheckAuthorityResponse
                .newBuilder()
                .setHasAccess(hasAccess)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
