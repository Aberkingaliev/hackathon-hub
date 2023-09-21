package com.hackathonhub.servicecontest.repositories;

import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Repository
public class ContestRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Contest save(Contest contest) {

        try (Session session = em.unwrap(Session.class);) {
            session.save("Contest",contest);
        }

        return contest;
    }

    @Transactional
    public Optional<Contest> findById(UUID id) {
        try (Session session = em.unwrap(Session.class)) {
            Contest foundedContest = session.find(Contest.class, id);
            return Optional.ofNullable(foundedContest);
        }
    }

    @Transactional
    public Optional<ContestDetailDto> getDetailById(UUID id) {

        try (Session session = em.unwrap(Session.class);) {
            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(),
                    em.getEntityGraph("Contest.ContestDetailed")
            );

            var res = session.find(Contest.class, id, properties);
            return Optional.of(new ContestDetailDto(res));
        }
    }


    @Transactional
    public Contest update(Contest contest) {

        try (Session session = em.unwrap(Session.class);) {
            session.update(contest);
        }

        return contest;
    }

    @Transactional
    public void deleteById(UUID id) {
        try (Session session = em.unwrap(Session.class);) {
            var contest = session.find(Contest.class, id);
            session.delete(contest);
        }
    }
}
