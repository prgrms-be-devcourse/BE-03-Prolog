package com.prgrms.prolog.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("""
		SELECT c
		FROM Comment c
		JOIN FETCH c.user
		WHERE c.id = :commentId
		""")
	Comment joinUserByCommentId(@Param(value = "commentId") Long commentId);
}
