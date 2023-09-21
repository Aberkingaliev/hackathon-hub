package com.hackathonhub.servicecontest.dtos.solution;


import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Data
public class SolutionCreateDto implements Serializable {

    private UUID teamId;
    private UUID contestId;
    private String name;
    private String description;
    private String url;

}

