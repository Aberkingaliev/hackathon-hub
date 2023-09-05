package com.hackathonhub.serviceteam.repositories;

import com.hackathonhub.serviceteam.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> { }
