package com.hackathonhub.serviceteam.models;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.PrePersist;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Table(name = "teams")
public class Team {

    @Id
    private UUID id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public Team setDescription(String description) {
        this.description = description;
        return this;
    }

    public Team setFounderId(UUID founderId) {
        this.founderId = founderId;
        return this;
    }

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;


    @Column(name = "founder_id")
    @NotNull
    private UUID founderId;

    @Column(name = "created_at")
    @CreatedDate
    @NotNull
    private Timestamp createdAt;
}