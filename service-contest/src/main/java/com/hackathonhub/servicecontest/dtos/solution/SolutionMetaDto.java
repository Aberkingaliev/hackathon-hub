package com.hackathonhub.servicecontest.dtos.solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SolutionMetaDto implements Serializable {

    private UUID id;
    private String name;
    private Date createdAt;

}
