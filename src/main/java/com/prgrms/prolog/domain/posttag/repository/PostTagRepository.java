package com.prgrms.prolog.domain.posttag.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.posttag.model.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	@Modifying
	@Query("""
		DELETE
		FROM PostTag pt
		WHERE pt.post.id = :postId
		AND pt.rootTag.id IN :rootTagIds
		""")
	void deleteByPostIdAndRootTagIds(
		@Param(value = "postId") Long postId,
		@Param(value = "rootTagIds") Set<Long> rootTagIds
	);

	@Query("""
		SELECT pt
		FROM PostTag pt
		LEFT JOIN FETCH pt.rootTag
		WHERE pt.post.id = :postId
		""")
	Set<PostTag> joinRootTagFindByPostId(@Param(value = "postId") Long postId);
}
