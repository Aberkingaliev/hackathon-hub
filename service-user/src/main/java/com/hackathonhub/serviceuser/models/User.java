package com.hackathonhub.serviceuser.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usersControl")
public class User {
    @Id
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

}