package com.hackathonhub.servicecontest.repositories;


import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class SolutionRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Set<SolutionMetaDto> getSolutionMetaListById(UUID contestId, Integer limit, UUID cursor) {

        try (Session session = em.unwrap(Session.class)) {
            var query = session.createQuery(
                            """
                                    SELECT new com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto
                                    (s.id, s.name, c.createdAt)
                                    FROM Solution s
                                    JOIN s.contest c
                                    WHERE c.id = :contest_id
                                    AND (CAST(:cursor as text) IS NULL OR s.id > :cursor)
                                    """, SolutionMetaDto.class)
                    .setParameter("contest_id", contestId)
                    .setParameter("cursor", cursor)
                    .setMaxResults(limit);

            List<SolutionMetaDto> result = query.getResultList();

            return new HashSet<>(result);
        }
    }

    @Transactional
    public Solution save(Solution solution) {
        try (Session session = em.unwrap(Session.class)) {
            session.persist(solution);
        }

        return solution;
    }

    @Transactional
    public Optional<Solution> findById(UUID id) {

        try (Session session = em.unwrap(Session.class)) {
            Solution foundedSolution = session.find(Solution.class, id);
            return Optional.ofNullable(foundedSolution);
        }
    }

    @Transactional
    public Solution update(Solution solution) {

        try (Session session = em.unwrap(Session.class)) {
            session.update(solution);
        }

        return solution;
    }

    @Transactional
    public void deleteById(UUID id) {
        try (Session session = em.unwrap(Session.class)) {
            Solution foundedSolution = em.find(Solution.class, id);
            session.remove(foundedSolution);
        }
    }
}
