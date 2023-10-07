package com.hackathonhub.serviceidentity.services.token;

import com.hackathonhub.common.grpc.Types;
import com.hackathonhub.identity_protos.grpc.IdentityServiceGrpc;
import com.hackathonhub.identity_protos.grpc.Messages;
import com.hackathonhub.serviceidentity.services.token.validators.AuthorityValidator;
import com.hackathonhub.serviceidentity.services.token.validators.ExpirationValidator;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
@Slf4j
public class TokenService extends IdentityServiceGrpc.IdentityServiceImplBase {

    private final TokenValidationManager tokenValidationManager;
    private final AuthorityValidator rolesValidator;
    private final ExpirationValidator expValidator;

    @Autowired
    public TokenService(final TokenValidationManager tokenValidationManager,
                        final AuthorityValidator rolesValidator,
                        final ExpirationValidator expValidator) {
        super();
        this.tokenValidationManager = tokenValidationManager;
        this.rolesValidator = rolesValidator;
        this.expValidator = expValidator;
    }


    @Override
    public void validateTokens(Messages.ValidateTokensRequest request,
                               StreamObserver<Messages.ValidateTokensResponse> responseObserver) {
        final TokenValidationContext context = TokenValidationContext
                .builder()
                    .route(request.getRoute())
                    .accessToken(request.getAccessToken())
                    .refreshToken(request.getRefreshToken())
                .build();

        try {
            final TokenValidationContext result = tokenValidationManager
                    .init(context)
                        .addValidator(rolesValidator)
                        .addValidator(expValidator)
                    .execute();

            final Messages.ValidateTokensResponse response = Messages.ValidateTokensResponse
                    .newBuilder()
                    .setIsValid(result.getIsValid())
                    .setStatus(
                            Types.HttpStatus.newBuilder()
                                    .setCode(result.getStatus().value())
                                    .build()
                    )
                    .setAccessToken(result.getAccessToken())
                    .setRefreshToken(result.getRefreshToken())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            log.error("Error while validating tokens: ", e);
            responseObserver.onError(e);
        }


    }
}
