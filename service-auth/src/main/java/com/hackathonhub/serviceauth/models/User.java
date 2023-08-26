package com.hackathonhub.serviceauth.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
public class User {

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    private UUID id;

    private String username;

    private String fullName;

    private String email;

    private String password;

    private Boolean isActivated;

    private UUID teamId;

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

    public User setActivated(Boolean activated) {
        isActivated = activated;
        return this;
    }

    public User setTeamId(UUID teamId) {
        this.teamId = teamId;
        return this;
    }

    public User setRole(Set<Role> roles) {
        this.roles = roles;
        return this;
    }
    public User from(User user) {
        return new User()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setActivated(user.getIsActivated())
                .setTeamId(user.getTeamId())
                .setRole(new HashSet<>(user.getRoles()));
    }

}