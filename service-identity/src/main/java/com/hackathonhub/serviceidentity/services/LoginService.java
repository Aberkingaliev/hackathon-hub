package com.hackathonhub.serviceidentity.services;

import com.hackathonhub.serviceidentity.config.auth.CustomAuthentication;
import com.hackathonhub.serviceidentity.constants.ApiAuthResponseMessage;
import com.hackathonhub.serviceidentity.dtos.ApiAuthResponse;
import com.hackathonhub.serviceidentity.dtos.UserLoginRequest;
import com.hackathonhub.serviceidentity.models.AuthToken;
import com.hackathonhub.serviceidentity.repositories.AuthRepository;
import com.hackathonhub.serviceidentity.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
    private final AuthRepository authRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authManager;

    public LoginService(AuthRepository authRepository,
                        JWTUtils jwtUtils,
                        AuthenticationManager authManager) {
        this.authRepository = authRepository;
        this.jwtUtils = jwtUtils;
        this.authManager = authManager;
    }

    public ApiAuthResponse<AuthToken> login(UserLoginRequest userLoginRequest) {
        ApiAuthResponse<AuthToken> responseBuilder = new ApiAuthResponse<>();

        try {
            CustomAuthentication authentication = (CustomAuthentication) authManager.authenticate(
                    new CustomAuthentication(
                            userLoginRequest.getEmail(),
                            userLoginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return responseBuilder.ok(saveTokens(authentication),
                    ApiAuthResponseMessage.USER_SUCCESS_AUTHORIZED);

        } catch (BadCredentialsException e) {
            log.info("Invalid password: {}", userLoginRequest.getEmail());
            return responseBuilder.unauthorized(ApiAuthResponseMessage.INVALID_PASSWORD);
        } catch (UsernameNotFoundException e) {
            log.info("User not found: {}", userLoginRequest.getEmail());
            return responseBuilder.notFound(ApiAuthResponseMessage.USER_NOT_FOUND);
        } catch (Exception e) {
            log.error("Login failed: ", e);
            return responseBuilder.internalServerError(e.getMessage());
        }

    }

    private AuthToken saveTokens(CustomAuthentication authentication) {
        String generatedAccessTokens = jwtUtils.generateAccessToken(authentication.getAuthorities(),
                authentication.getName());
        String generatedRefreshTokens = jwtUtils.generateRefreshToken(authentication.getName());

        AuthToken newTokens = new AuthToken(authentication.getUserId(),
                generatedAccessTokens, generatedRefreshTokens);

        return authRepository.save(newTokens);
    }
}
