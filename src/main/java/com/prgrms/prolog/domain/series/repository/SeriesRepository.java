package com.prgrms.prolog.domain.series.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.series.model.Series;

public interface SeriesRepository extends JpaRepository<Series, Long> {

	@Query("""
		SELECT s
		FROM Series s
		JOIN FETCH s.posts
		WHERE s.user.id = :userId
		""")
	List<Series> joinPostFindByUserId(@Param(value = "userId") Long userId);

	@Query("""
		SELECT s
		FROM Series s
		JOIN FETCH s.posts
		WHERE s.user.id = :userId
		AND s.id = :seriesId
		""")
	Optional<Series> joinPostFindByIdAndUserId(
		@Param(value = "userId") Long userId,
		@Param(value = "seriesId") Long seriesId);

	@Modifying
	@Query("""
		UPDATE Series s
		SET s.deleted = true
		WHERE s.user.id = :userId
		AND s.id = :seriesId
		""")
	int deleteByIdAndUserId(
		@Param(value = "userId") Long userId,
		@Param(value = "seriesId") Long seriesId);
}
