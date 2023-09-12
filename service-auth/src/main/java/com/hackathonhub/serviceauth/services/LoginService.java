package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.constants.AuthApiResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.services.security.UserDetailsImpl;
import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@Service
public class LoginService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ApiAuthResponse<AuthToken> login(UserLoginRequest userLoginRequest) {
        ApiAuthResponse.ApiAuthResponseBuilder<AuthToken> responseBuilder =  ApiAuthResponse
                .builder();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getEmail(),
                            userLoginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HashMap<String, String> generatedTokens = jwtUtils.generateToken(
                    authentication
                            .getPrincipal()
                            .toString(),
                    authentication.getAuthorities()
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                    .loadUserByUsername(userLoginRequest.getEmail());

            AuthToken newTokens = new AuthToken()
                    .setUserId(userDetails.getId())
                    .setRefreshToken(generatedTokens.get("refreshToken"))
                    .setAccessToken(generatedTokens.get("accessToken"))
                    .setCreatedAt(System.currentTimeMillis());

            AuthToken savedTokens = authRepository.save(newTokens);

            return responseBuilder
                    .message(AuthApiResponseMessage.USER_SUCCESS_AUTHORIZED)
                    .status(HttpStatus.OK)
                    .data(savedTokens)
                    .build();

        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return responseBuilder
                    .message(AuthApiResponseMessage.authenticationFailed(e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build();
        }

    }
}
