package com.hackathonhub.serviceidentity.config.auth;

import org.springframework.security.core.context.SecurityContextImpl;

import java.util.UUID;

public class CustomSecurityContextHolder extends SecurityContextImpl {
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
