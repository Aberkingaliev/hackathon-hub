package com.hackathonhub.servicecontest.dtos.contest;

import com.hackathonhub.servicecontest.models.contest.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ContestUpdateDto {

    private UUID id;
    private String name;
    private String description;
    private ContestStatus status;
    private Date endDate;
}
