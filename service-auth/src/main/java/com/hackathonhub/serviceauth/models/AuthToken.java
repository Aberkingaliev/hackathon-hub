package com.hackathonhub.serviceauth.models;

import javax.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "auth_tokens", uniqueConstraints = {
        @UniqueConstraint(columnNames = "refresh_token")
})
public class AuthToken implements Serializable {
    @Id
    protected UUID id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public AuthToken setId(UUID id) {
        this.id = id;
        return this;
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

    @Column(name = "user_id")
    protected UUID userId;

    @Column(name = "access_token", columnDefinition = "TEXT", length = 7000)
    protected String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT", length = 7000)
    protected String refreshToken;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created_at")
    protected Timestamp createdAt;
}