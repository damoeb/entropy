package org.migor.entropy.repository;

import org.migor.entropy.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Vote entity.
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
