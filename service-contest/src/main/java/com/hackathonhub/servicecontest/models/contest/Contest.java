package com.hackathonhub.servicecontest.models.contest;

import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestUpdateDto;
import com.hackathonhub.servicecontest.models.User;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "contests")
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "Contest.ContestDetailed",
        attributeNodes = {
                @NamedAttributeNode("owner"),
                @NamedAttributeNode("categories"),
        }
)
public class Contest implements Serializable {

        @Id
        private UUID id;

        @PrePersist
        public void prePersist() {
            this.id = UUID.randomUUID();
            this.status = ContestStatus.OPEN_TO_SOLUTIONS;
            this.createdAt = new Date();
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "owner_id", insertable = true, updatable = false)
        private User owner;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "contest_to_category",
                joinColumns = @JoinColumn(name = "contest_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private Set<ContestCategory> categories = new HashSet<>();

        @Column(name = "name")
        private String name;

        @Column(name = "description")
        private String description;

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        private ContestStatus status;

        @Column(name = "end_date")
        private Date endDate;

        @Column(name = "created_at")
        private Date createdAt;


    public static Contest fromCreateDto(ContestCreateDto contest) {
        return new Contest()
                .setOwner(new User().setId(contest.getOwnerId()))
                .setName(contest.getName())
                .setDescription(contest.getDescription())
                .setCategories(contest.getCategories())
                .setEndDate(contest.getEndDate()
        );
    }

    public Contest fromUpdateDto(ContestUpdateDto contest) {
        this.name = contest.getName();
        this.description = contest.getDescription();
        this.status = contest.getStatus();
        this.endDate = contest.getEndDate();
        return this;
    }

}
