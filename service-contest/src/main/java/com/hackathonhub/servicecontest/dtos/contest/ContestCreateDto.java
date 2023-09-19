package com.hackathonhub.servicecontest.dtos.contest;


import com.hackathonhub.servicecontest.models.contest.ContestCategory;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

@Data
@Getter
public class ContestCreateDto implements Serializable {

    private UUID ownerId;
    private String name;
    private String description;
    private Set<ContestCategory> categories;
    private Date endDate;
}
