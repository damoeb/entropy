package org.migor.entropy.repository;

import org.migor.entropy.domain.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Thread entity.
 */
public interface ThreadRepository extends JpaRepository<Thread, Long> {

    Thread findByUri(String uri);
}
