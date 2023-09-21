package com.hackathonhub.servicecontest.models.contest;


import com.hackathonhub.servicecontest.dtos.contest.ContestCategoryCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "contest_category")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContestCategory implements Serializable {

    @Id
    private UUID id;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
    }

    @Column(name = "category_name")
    @Size(max = 255)
    private String categoryName;

    public ContestCategory fromCreateDto(ContestCategoryCreateDto contestCategoryCreateDto) {
        return new ContestCategory()
                .setCategoryName(contestCategoryCreateDto.getCategoryName());
    }
}
