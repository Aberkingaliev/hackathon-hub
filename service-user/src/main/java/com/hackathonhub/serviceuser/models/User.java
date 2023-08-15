package com.hackathonhub.serviceuser.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "isActivated")
    private Boolean isActivated;

    @Column(name = "teamId")
    private UUID teamId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.user;

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

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