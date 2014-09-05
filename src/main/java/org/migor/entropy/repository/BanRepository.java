package org.migor.entropy.repository;

import org.joda.time.DateTime;
import org.migor.entropy.domain.Ban;
import org.migor.entropy.domain.BanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Ban entity.
 */
public interface BanRepository extends JpaRepository<Ban, Long> {

    List<Ban> findByTypeAndExpirationAfter(BanType banType, DateTime expiration);

    Ban findByTypeAndExpression(BanType userId, String currentLogin);
}
