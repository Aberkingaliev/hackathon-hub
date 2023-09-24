package com.hackathonhub.serviceteam.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TeamCreateDto {

    private String name;
    private String description;
    private UUID founderId;
}
