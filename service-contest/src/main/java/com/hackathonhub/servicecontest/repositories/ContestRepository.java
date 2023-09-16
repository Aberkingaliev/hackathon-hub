package com.hackathonhub.servicecontest.repositories;

import com.hackathonhub.servicecontest.models.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContestRepository extends JpaRepository<Contest, UUID> {
}
