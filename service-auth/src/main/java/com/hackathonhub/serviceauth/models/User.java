package com.hackathonhub.serviceauth.models;

import com.hackathonhub.serviceauth.dtos.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
public class User implements Serializable {

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private String username;

    private String fullName;

    private String email;

    private String password;

    private Boolean isActivated;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<Role> roles = new HashSet<>();


    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setIsActivated(Boolean activated) {
        isActivated = activated;
        return this;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

}