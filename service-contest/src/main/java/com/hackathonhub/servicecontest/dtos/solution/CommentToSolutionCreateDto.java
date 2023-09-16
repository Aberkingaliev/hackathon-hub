package com.hackathonhub.servicecontest.dtos.solution;


import com.hackathonhub.servicecontest.models.User;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@Data
public class CommentToSolutionCreateDto {

        private UUID solutionId;
        private UUID authorId;
        private String comment;

}
