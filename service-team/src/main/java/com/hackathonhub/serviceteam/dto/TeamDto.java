package com.hackathonhub.serviceteam.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class TeamDto implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private UUID founderId;
    private Date createdAt;
}
