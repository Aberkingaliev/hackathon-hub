package com.hackathonhub.servicecontest.repositories;

import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ContestRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Contest save(Contest contest) {
        try (Session session = em.unwrap(Session.class);) {
            session.save("Contest",contest);
            return contest;
        } catch (Exception e) {
            log.error("Error while saving contest: {}", e.getMessage());
            return null;
        }
    }

    @Transactional
    public Optional<ContestDetailDto> getContestDetailById(UUID id) {

        try (Session session = em.unwrap(Session.class);) {
            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(),
                    em.getEntityGraph("Contest.ContestDto")
            );

            var res = session.find(Contest.class, id, properties);
            return Optional.of(new ContestDetailDto(res));
        } catch (Exception e) {
            log.error("Error while getting contest: {}", e.getMessage());
            return Optional.empty();
        }
    }


    @Transactional
    public Contest updateContest(Contest contest) {

        try (Session session = em.unwrap(Session.class);) {
            session.update(contest);
        } catch (Exception e) {
            log.error("Error while getting contest: {}", e.getMessage());
        }

        return contest;
    }

    @Transactional
    public void deleteContestById(UUID id) {
        try (Session session = em.unwrap(Session.class);) {
            var contest = session.find(Contest.class, id);
            session.delete(contest);
        } catch (Exception e) {
            log.error("Error while deleting contest: {}", e.getMessage());
        }
    }
}
