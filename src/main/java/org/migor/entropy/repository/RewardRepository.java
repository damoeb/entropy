package org.migor.entropy.repository;

import org.migor.entropy.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Reward entity.
 */
public interface RewardRepository extends JpaRepository<Reward, Long> {

}
