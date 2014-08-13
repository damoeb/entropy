package org.migor.entropy.repository;

import org.migor.entropy.domain.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Lock entity.
 */
public interface LockRepository extends JpaRepository<Lock, Long> {

}
