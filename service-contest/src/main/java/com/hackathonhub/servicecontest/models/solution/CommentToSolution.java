package com.hackathonhub.servicecontest.models.solution;

import com.hackathonhub.servicecontest.models.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "comment_to_solution")
@Data
public class CommentToSolution {

    @Id
    private UUID id;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", insertable = true, updatable = false)
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = true, updatable = false)
    private User author;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private Date createdAt;

}
