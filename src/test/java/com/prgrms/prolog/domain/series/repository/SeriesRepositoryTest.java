package com.prgrms.prolog.domain.series.repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.series.model.Series;

public class SeriesRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("해당 유저가 가진 시리즈 중에서 찾는 제목의 시리즈를 조회한다.")
	void findByIdAndTitleTest() {
		// given & when
		Optional<Series> series = seriesRepository.findByIdAndTitle(savedUser.getId(), SERIES_TITLE);
		// then
		assertThat(series).isPresent();
	}

}
