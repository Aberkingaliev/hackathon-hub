package com.hackathonhub.serviceidentity.services.token.validators;

import com.hackathonhub.serviceidentity.dtos.UserDto;
import com.hackathonhub.serviceidentity.models.AuthToken;
import com.hackathonhub.serviceidentity.repositories.AuthRepository;
import com.hackathonhub.serviceidentity.services.token.TokenValidationContext;
import com.hackathonhub.serviceidentity.services.security.UserDetailsServiceImpl;
import com.hackathonhub.serviceidentity.utils.JWTUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@NoArgsConstructor
@Component
public class ExpirationValidator implements Validator {
    private JWTUtils jwtUtils;
    private AuthRepository authRepository;
    private UserDetailsServiceImpl userDetails;

    @Autowired
    public ExpirationValidator(JWTUtils jwtUtils,
                               AuthRepository authRepository,
                               UserDetailsServiceImpl userDetails) {
        this.jwtUtils = jwtUtils;
        this.authRepository = authRepository;
        this.userDetails = userDetails;
    }


    /**
     * This method checks whether the tokens are expired or not
     * If accessToken, is valid, then the status is set to OK.
     * If accessToken is expired but refreshToken is valid, it will +
     * + generate a new pair and set the status to OK.
     * If both are expired, it will set the status to UNAUTHENTICATED.
     * @param context - @see {@link TokenValidationContext}
     */
    @Override
    public void doValidation(TokenValidationContext context) {
        if(isAccessTokenValid(context)) {
            context.setStatus(HttpStatus.OK);
            context.setIsValid(true);
            return;
        }

        if(!isRefreshTokenValid(context)) {
            context.setStatus(HttpStatus.UNAUTHORIZED);
            context.setIsValid(false);
            return;
        }

        AuthToken updatedTokens = refreshTokens(context);
        context.setAccessToken(updatedTokens.getAccessToken());
        context.setRefreshToken(updatedTokens.getRefreshToken());
        context.setStatus(HttpStatus.OK);
        context.setIsValid(true);
    }

    private boolean isAccessTokenValid(TokenValidationContext context) {
        return jwtUtils.validateToken(context.getAccessToken());
    }

    private boolean isRefreshTokenValid(TokenValidationContext context) {
        return jwtUtils.validateToken(context.getRefreshToken()) &&
                authRepository.findByRefreshToken(context.getRefreshToken()).isPresent();
    }

    private AuthToken refreshTokens(TokenValidationContext context) {
        final Optional<AuthToken> authToken = authRepository.findByRefreshToken(context.getRefreshToken());
        final UserDto user = userDetails.loadUserById(authToken.get().getId());
        return updateToken(user, authToken.get());
    }


    private AuthToken updateToken(UserDto user,
                                  AuthToken foundedToken) {
        final Collection<? extends GrantedAuthority> rolesList =
                user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString()))
                        .toList();

        final String newAccessToken = jwtUtils.generateAccessToken(rolesList, user.getEmail());
        final String newRefreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        foundedToken.setAccessToken(newAccessToken);
        foundedToken.setRefreshToken(newRefreshToken);

        return authRepository.save(foundedToken);
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
