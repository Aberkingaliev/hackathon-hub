package com.hackathonhub.serviceteam.repositories;

import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {

    @Query(value = "SELECT t.user FROM TeamMember t WHERE t.id.teamId = :teamId " +
            "AND (CAST(:cursor as text) IS NULL OR t.id.userId > :cursor) " +
            "ORDER BY t.user.username ASC")
    List<User> findMembersByTeamId(@Param("teamId") UUID teamId,
                                 @Param("cursor") UUID cursor,
                                 Pageable pageable);
}
