package com.hackathonhub.serviceuser.models;

import javax.persistence.*;

import com.hackathonhub.serviceuser.dtos.UserDto;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
public class User implements Serializable {

    @Id
    private UUID id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_activated")
    private Boolean isActivated;

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

    public User setIsActivated(Boolean activated) {
        isActivated = activated;
        return this;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User fromDto(UserDto userDto) {
        this.id = userDto.getId();
        this.username = userDto.getUsername();
        this.fullName = userDto.getFullName();
        this.email = userDto.getEmail();
        this.isActivated = userDto.getIsActivated();
        this.roles = userDto.getRoles();
        return this;
    }

    public UserDto toDto() {
        return new UserDto()
                .setId(this.id)
                .setUsername(this.username)
                .setFullName(this.fullName)
                .setEmail(this.email)
                .setIsActivated(this.isActivated)
                .setRoles(this.roles);
    }

}