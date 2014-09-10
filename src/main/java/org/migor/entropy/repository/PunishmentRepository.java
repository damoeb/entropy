package org.migor.entropy.repository;

import org.migor.entropy.domain.Punishment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Punishment entity.
 */
public interface PunishmentRepository extends JpaRepository<Punishment, Long> {

}
