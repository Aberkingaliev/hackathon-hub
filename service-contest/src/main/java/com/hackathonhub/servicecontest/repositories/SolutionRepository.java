package com.hackathonhub.servicecontest.repositories;


import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Repository
@Slf4j
public class SolutionRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Set<SolutionMetaDto> getSolutionsMetaByContestId(UUID contestId, Integer limit, UUID cursor) {

        try (Session session = em.unwrap(Session.class)) {
            var query = session.createQuery(
                    """
                            SELECT new com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto
                            (s.id, s.name, c.createdAt)
                            FROM Solution s
                            JOIN s.contest c
                            WHERE c.id = :contest_id
                            AND (CAST(:cursor as text) IS NULL OR s.id > :cursor)
                            """, SolutionMetaDto.class
            )
                    .setParameter("contest_id", contestId)
                    .setParameter("cursor", cursor)
                    .setMaxResults(limit);

            List<SolutionMetaDto> result = query.getResultList();

            return new HashSet<>(result);
        } catch (Exception e) {
            log.error("Error while getting solutions: {}", e.getMessage());
            return new HashSet<>();
        }

    }
}
