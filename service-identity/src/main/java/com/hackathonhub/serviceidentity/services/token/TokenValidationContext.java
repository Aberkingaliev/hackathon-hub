package com.hackathonhub.serviceidentity.services.token;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class TokenValidationContext {
    @Builder.Default
    private Boolean isValid = false;
    private HttpStatus status;
    private String accessToken;
    private String refreshToken;
    private String route;


    public Boolean isEmpty() {
        return accessToken == null || refreshToken == null;
    }
}
