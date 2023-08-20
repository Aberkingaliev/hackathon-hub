package com.hackathonhub.serviceauth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;




@Entity
@Data
@Table(name = "authTokens", uniqueConstraints = {
        @UniqueConstraint(columnNames = "refreshToken")
})
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

    public AuthToken setCreatedAt(Long createdAt) {
        this.createdAt = Timestamp.from(Instant.ofEpochSecond(createdAt));
        return this;
    }

    @Column(name = "userId")
    protected UUID userId;

    @Column(name = "accessToken", columnDefinition = "TEXT", length = 7000)
    protected String accessToken;

    @Column(name = "refreshToken", columnDefinition = "TEXT", length = 7000)
    protected String refreshToken;

    @Column(name = "createdAt")
    protected Timestamp createdAt;
}