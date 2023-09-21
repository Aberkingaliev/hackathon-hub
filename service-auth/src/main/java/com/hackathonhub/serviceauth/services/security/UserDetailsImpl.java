package com.hackathonhub.serviceauth.services.security;

import com.hackathonhub.serviceauth.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class UserDetailsImpl implements UserDetails {

    public UserDetailsImpl(UUID id, String username, String email, String password, Boolean isActivated, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActivated = isActivated;
        this.authorities = authorities;
    }


    public Boolean getActivated() {
        return isActivated;
    }

    @Getter
    private UUID id;
    private String username;
    @Getter
    private String email;
    private String password;
    private Boolean isActivated;
    private Collection<? extends GrantedAuthority> authorities;


    public static UserDetailsImpl build (User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).toList();

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getIsActivated(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }
}
