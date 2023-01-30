package com.prgrms.prolog.domain.usertag.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.usertag.model.UserTag;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {

	@Query("""
		SELECT ut
		FROM UserTag ut
		WHERE ut.user.id = :userId
		AND ut.rootTag.id IN :rootTagIds
		""")
	Set<UserTag> findByUserIdAndInRootTagIds(
		@Param(value = "userId") Long userId,
		@Param(value = "rootTagIds") List<Long> rootTagIds
	);
}
