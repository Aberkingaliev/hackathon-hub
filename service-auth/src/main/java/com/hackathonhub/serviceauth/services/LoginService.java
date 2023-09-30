package com.hackathonhub.serviceauth.services;

import com.hackathonhub.serviceauth.constants.ApiAuthResponseMessage;
import com.hackathonhub.serviceauth.dtos.ApiAuthResponse;
import com.hackathonhub.serviceauth.dtos.UserLoginRequest;
import com.hackathonhub.serviceauth.models.AuthToken;
import com.hackathonhub.serviceauth.repositories.AuthRepository;
import com.hackathonhub.serviceauth.services.security.UserDetailsImpl;
import com.hackathonhub.serviceauth.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceauth.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;


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
        ApiAuthResponse<AuthToken> responseBuilder = new ApiAuthResponse<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getEmail(),
                            userLoginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Map<String, String> generatedTokens = jwtUtils.generateToken(
                    authentication
                            .getPrincipal()
                            .toString(),
                    authentication.getAuthorities()
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                    .loadUserByUsername(userLoginRequest.getEmail());

            AuthToken newTokens = new AuthToken(userDetails.getId(),
                    generatedTokens.get("refreshToken"), generatedTokens.get("accessToken"));

            AuthToken savedTokens = authRepository.save(newTokens);

            return responseBuilder.ok(savedTokens, ApiAuthResponseMessage.USER_SUCCESS_AUTHORIZED);

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
}
