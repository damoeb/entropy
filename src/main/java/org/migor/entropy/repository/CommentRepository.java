package org.migor.entropy.repository;

import org.migor.entropy.domain.Comment;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
