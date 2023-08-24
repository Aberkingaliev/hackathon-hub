package com.hackathonhub.serviceuser.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
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

    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

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

    public User setRole(HashSet<Role> roles) {
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