package com.hackathonhub.serviceidentity.services.token.validators;

import com.hackathonhub.serviceidentity.constants.ApiRolePermission;
import com.hackathonhub.serviceidentity.models.RoleEnum;
import com.hackathonhub.serviceidentity.services.token.TokenValidationContext;
import com.hackathonhub.serviceidentity.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class AuthorityValidator implements Validator {

    private final JWTUtils jwtUtils;

    @Autowired
    public AuthorityValidator(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    /**
     * The method checks if the role and the protected endpoint to which the client is accessing match
     * If they match, the status is set to OK.
     * If they don't match, the status is set to PERMISSION_DENIED.
     * @param context - @see {@link TokenValidationContext}
     */
    @Override
    public void doValidation(TokenValidationContext context) {
        final Set<RoleEnum> roleEnums = jwtUtils.extractRolesFromToken(context.getAccessToken());
        final boolean isRoleAllowed = ApiRolePermission.isRoleAllowed(context.getRoute(), roleEnums);
        context.setStatus(isRoleAllowed ? HttpStatus.OK : HttpStatus.FORBIDDEN);
        context.setIsValid(isRoleAllowed);
    }

    @Override
    public int getPriority() {
        return 0;
    }
}