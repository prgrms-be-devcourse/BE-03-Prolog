package com.prgrms.prolog.domain.posttag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.posttag.model.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	@Query("""
		DELETE
		FROM PostTag pt
		WHERE pt.post.id = :postId
		AND pt.rootTag.id IN :rootTagIds
		""")
	void deleteByPostIdAndRootTagIds(
		@Param(value = "postId") Long postId,
		@Param(value = "rootTagIds") List<Long> rootTagIds
	);

	@Query("""
		SELECT pt
		FROM PostTag pt
		LEFT JOIN FETCH pt.rootTag
		WHERE pt.post.id = :postId
		""")
	List<PostTag> joinPostTagFindByPostId(@Param(value = "postId") Long postId);
}
