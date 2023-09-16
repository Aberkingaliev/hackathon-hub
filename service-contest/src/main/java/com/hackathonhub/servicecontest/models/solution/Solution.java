package com.hackathonhub.servicecontest.models.solution;


import com.hackathonhub.servicecontest.models.Team;
import com.hackathonhub.servicecontest.models.contest.Contest;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "solutions")
@Data
public class Solution {

    @Id
    private UUID id;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.status = SolutionStatus.PENDING;
        this.createdAt = new Date();
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SolutionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", insertable = true, updatable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", insertable = true, updatable = false)
    private Contest contest;

    @Column(name = "name")
    @Size(max = 255)
    @NotNull
    private String name;

    @Column(name = "description")
    @Size(min = 50)
    @NotNull
    private String description;

    @Column(name = "url")
    @Null
    private String url;

    @Column(name = "created_at")
    private Date createdAt;

}
