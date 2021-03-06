package org.migor.entropy.repository;

import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByThreadIdAndStatus(Long threadId, Comment.Status status);

    @Query("select c from Comment c where c.threadId = ?1 and exists (select r from Report r where c.id = r.commentId and r.status = ?2)")
    List<Comment> findByThreadIdAndReportStatus(Long id, Report.Status status);

    @Query("select count(c) from Comment c where c.threadId = ?1 and c.status = ?2")
    Integer getCountForThreadIdAndStatus(Long id, Comment.Status status);

    @Query("select count(c) from Comment c where c.threadId = ?1 and exists (select r from Report r where c.id = r.commentId and r.status = ?2)")
    Integer getReportCountForThreadIdAndReportStatus(Long id, Report.Status status);
}
