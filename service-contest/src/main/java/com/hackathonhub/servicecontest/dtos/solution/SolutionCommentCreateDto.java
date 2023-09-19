package com.hackathonhub.servicecontest.dtos.solution;


import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@Data
public class SolutionCommentCreateDto {

        private UUID solutionId;
        private UUID authorId;
        private String comment;

}
