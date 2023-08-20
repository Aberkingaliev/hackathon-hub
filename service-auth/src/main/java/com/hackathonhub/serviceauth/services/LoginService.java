package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.dtos.contexts.ApiResponseDataContext;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.services.security.UserDetailsImpl;
import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Optional;

@Service
public class LoginService {


    public LoginService(AuthRepository authRepository, UserDetailsServiceImpl userDetails, JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.authRepository = authRepository;
        this.userDetailsService = userDetails;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    private final AuthRepository authRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public ApiAuthResponse login(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getEmail(),
                        userLoginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        HashMap<String, String> generatedTokens = jwtUtils.generateToken(authentication.getPrincipal().toString());

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userLoginRequest.getEmail());

        AuthToken newTokens = new AuthToken()
                .setUserId(userDetails.getId())
                .setRefreshToken(generatedTokens.get("refreshToken"))
                .setAccessToken(generatedTokens.get("accessToken"))
                .setCreatedAt(System.currentTimeMillis());

        AuthToken savedTokens = authRepository.save(newTokens);

        ApiResponseDataContext apiResponseDataContext = ApiResponseDataContext
                .builder()
                .data(Optional.of(savedTokens))
                .build();

        return ApiAuthResponse
                .builder()
                .status(HttpStatus.OK)
                .message("USER_SUCCESS_AUTHORIZED")
                .data(apiResponseDataContext)
                .build();

    }
}
