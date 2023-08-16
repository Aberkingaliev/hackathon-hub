package com.hackathonhub.serviceauth.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;




@Entity
@Data
@Table(name = "authTokens")
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public AuthToken setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public AuthToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AuthToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthToken setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Column(name = "userId")
    protected UUID userId;

    @Column(name = "accessToken")
    protected String accessToken;

    @Column(name = "refreshToken")
    protected String refreshToken;

    @Column(name = "createdAt")
    protected Date createdAt;
}