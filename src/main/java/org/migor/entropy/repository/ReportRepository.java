package org.migor.entropy.repository;

import org.migor.entropy.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Report entity.
 */
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByThreadIdAndStatus(Long threadId, Report.Status status);

    Report findByCommentIdAndAuthorId(Long commentId, String authorId);
}
