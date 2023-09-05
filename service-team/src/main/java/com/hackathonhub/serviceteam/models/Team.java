package com.hackathonhub.serviceteam.models;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@ToString
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Team implements Serializable {

    @Id
    private UUID id;

    @PrePersist
    public void prePersistValues() {
        this.id = UUID.randomUUID();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "name")
    private String name;

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description")
    private String description;

    public Team setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "founder_id")
    private UUID founderId;

    public Team setFounderId(UUID founderId) {
        this.founderId = founderId;
        return this;
    }

    @Column(name = "created_at")
    private Timestamp createdAt;
}