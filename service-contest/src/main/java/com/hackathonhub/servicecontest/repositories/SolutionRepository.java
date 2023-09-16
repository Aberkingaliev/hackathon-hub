package com.hackathonhub.servicecontest.repositories;

import com.hackathonhub.servicecontest.models.solution.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {};
