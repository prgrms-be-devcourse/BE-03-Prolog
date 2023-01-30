package com.prgrms.prolog.domain.series.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.prolog.domain.series.model.Series;

public interface SeriesRepository extends JpaRepository<Series, Long> {

	@Query("""
  		SELECT s
		FROM Series s
		WHERE s.user.id = :userId
		and s.title = :title
		""")
	Optional<Series> findByIdAndTitle(@Param(value = "userId") Long userId, @Param(value = "title") String title);

}
