package com.hackathonhub.servicecontest.dtos.solution;

import com.hackathonhub.servicecontest.models.solution.SolutionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SolutionUpdateDto {
    private UUID id;
    private String name;
    private String description;
    private SolutionStatus status;
    private String url;

}
