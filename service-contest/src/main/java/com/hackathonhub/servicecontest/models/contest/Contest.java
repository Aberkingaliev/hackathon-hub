package com.hackathonhub.servicecontest.models.contest;

import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.models.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "contests")
@Data
public class Contest {

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
    private Set<ContestCategory> categories = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ContestStatus status;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Set<Solution> solutions;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "created_at")
    private Date createdAt;
}
