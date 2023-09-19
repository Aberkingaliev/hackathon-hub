package com.hackathonhub.servicecontest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@ToString
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team implements Serializable {

    public Team setId(UUID id) {
        this.id = id;
        return this;
    }

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    private UUID id;

    @PrePersist
    public void prePersistValues() {
        this.id = UUID.randomUUID();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "name")
    private String name;


    @Column(name = "description")
    private String description;


    @Column(name = "founder_id")
    private UUID founderId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created_at")
    private Timestamp createdAt;
}