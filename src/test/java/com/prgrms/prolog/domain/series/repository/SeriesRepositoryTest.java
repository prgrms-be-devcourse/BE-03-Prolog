package com.prgrms.prolog.domain.series.repository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.series.model.Series;

public class SeriesRepositoryTest extends RepositoryTest {

	@Autowired
	private SeriesRepository seriesRepository;

	@Test
	@DisplayName("해당 유저가 가진 시리즈 중에서 찾는 제목의 시리즈를 조회한다.")
	void findByIdAndTitleTest() {

		final Optional<Series> series = seriesRepository.joinPostFindByUserIdAndSeriesId(1L, 1L);
	}
}
