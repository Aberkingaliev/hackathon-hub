package com.hackathonhub.serviceidentity.config.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class CustomAuthentication extends UsernamePasswordAuthenticationToken {
    private UUID userId;

    public CustomAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public CustomAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public CustomAuthentication(Object principal,
                                Object credentials,
                                Collection<? extends GrantedAuthority> authorities,
                                UUID userId) {
        super(principal, credentials, authorities);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
