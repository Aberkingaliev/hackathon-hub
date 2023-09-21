package com.hackathonhub.servicecontest.dtos.contest;

import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.User;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.models.contest.ContestCategory;
import com.hackathonhub.servicecontest.models.contest.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.Data;

import java.io.Serializable;
import java.util.*;




@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestDetailDto implements Serializable {

    private UUID id;
    private User owner;
    private String name;
    private String description;
    private ContestStatus status;
    private Set<ContestCategory> categories;
    private Set<SolutionMetaDto> solutions;
    private Date endDate;
    private Date createdAt;

    public ContestDetailDto (Contest contest) {
        this.id = contest.getId();
        this.owner = contest.getOwner();
        this.name = contest.getName();
        this.description = contest.getDescription();
        this.status = contest.getStatus();
        this.categories = contest.getCategories();
        this.endDate = contest.getEndDate();
        this.createdAt = contest.getCreatedAt();
    }


}
