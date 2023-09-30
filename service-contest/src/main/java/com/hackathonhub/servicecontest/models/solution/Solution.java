package com.hackathonhub.servicecontest.models.solution;


import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionUpdateDto;
import com.hackathonhub.servicecontest.models.Team;
import com.hackathonhub.servicecontest.models.contest.Contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "solutions")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution implements Serializable {

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

    public static Solution fromCreateDto(SolutionCreateDto solutionCreateDto) {
        return new Solution()
                .setTeam(new Team().setId(solutionCreateDto.getTeamId()))
                .setContest(new Contest().setId(solutionCreateDto.getContestId()))
                .setName(solutionCreateDto.getName())
                .setDescription(solutionCreateDto.getDescription())
                .setUrl(solutionCreateDto.getUrl());
    }

    public Solution fromUpdateDto(SolutionUpdateDto solutionUpdateDto) {
        return this
                .setName(solutionUpdateDto.getName())
                .setDescription(solutionUpdateDto.getDescription())
                .setStatus(solutionUpdateDto.getStatus())
                .setUrl(solutionUpdateDto.getUrl());
    }

}
