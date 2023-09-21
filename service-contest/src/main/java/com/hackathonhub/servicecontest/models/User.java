package com.hackathonhub.servicecontest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User from(User user) {
        return new User()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActivated(user.getIsActivated())
                .setRoles(new HashSet<>(user.getRoles()));
    }
}