package org.migor.entropy.repository;

import org.migor.entropy.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Report entity.
 */
public interface ReportRepository extends JpaRepository<Report, Long> {

}
