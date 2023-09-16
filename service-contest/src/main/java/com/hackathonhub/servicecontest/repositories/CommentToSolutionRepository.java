package com.hackathonhub.servicecontest.repositories;

import com.hackathonhub.servicecontest.models.solution.CommentToSolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentToSolutionRepository extends JpaRepository<CommentToSolution, UUID> {
}
