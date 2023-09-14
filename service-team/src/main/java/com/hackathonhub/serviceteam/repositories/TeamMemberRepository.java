package com.hackathonhub.serviceteam.repositories;

import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> { }
