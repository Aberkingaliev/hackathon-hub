package com.hackathonhub.serviceauth.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
public class User {

    private String username;

    private String fullName;

    private String email;

    private String password;

    private Boolean isActivated;

    private UUID teamId;

    private Role role = Role.user;


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

    public User setActivated(Boolean activated) {
        isActivated = activated;
        return this;
    }

    public User setTeamId(UUID teamId) {
        this.teamId = teamId;
        return this;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }
}