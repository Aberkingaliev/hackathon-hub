package com.hackathonhub.servicecontest.models.contest;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "contest_category")
@Data
public class ContestCategory {

    @Id
    private UUID id;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
    }

    @Column(name = "category_name")
    @Size(max = 255)
    private String categoryName;
}
