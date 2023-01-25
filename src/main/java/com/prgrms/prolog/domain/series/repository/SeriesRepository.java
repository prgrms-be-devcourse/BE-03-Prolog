package com.prgrms.prolog.domain.series.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.prolog.domain.series.model.Series;

public interface SeriesRepository extends JpaRepository<Series, Long> {

	// @Query("""
	//	title & userId가 같은 Series, 한방에 잘 가져올 수 없나..?
	// 	""")
	Optional<Series> findByIdAndTitle(Long userId, String title);

}
