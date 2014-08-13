package org.migor.entropy.repository;

import org.migor.entropy.domain.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Ban entity.
 */
public interface BanRepository extends JpaRepository<Ban, Long> {

}
