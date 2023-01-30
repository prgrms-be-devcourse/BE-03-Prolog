package com.prgrms.prolog.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prgrms.prolog.domain.post.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("""
		SELECT p
		FROM Post p
		LEFT JOIN FETCH p.comments c
		where p.id = :postId
		""")
	Optional<Post> joinCommentFindById(@Param(value = "postId") Long postId);

	@Query("""
		SELECT p
		FROM Post p
		LEFT JOIN FETCH p.user
		WHERE p.id = :postId
		""")
	Optional<Post> joinUserFindById(@Param(value = "postId") Long postId);

	@Modifying
	@Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
	int addLikeCount(@Param(value = "postId") Long postId);

	@Modifying
	@Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
	int subLikeCount(@Param(value = "postId") Long postId);
}
