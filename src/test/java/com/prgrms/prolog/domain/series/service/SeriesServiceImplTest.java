package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.prgrms.prolog.base.ServiceTest;
import com.prgrms.prolog.domain.series.model.Series;

class SeriesServiceImplTest extends ServiceTest {

	private static final Long POST_ID = 1L;
	private static final CreateSeriesRequest createSeriesRequest
		= new CreateSeriesRequest(SERIES_TITLE);

	@InjectMocks
	private SeriesServiceImpl seriesService;

	@Test
	@DisplayName("시리즈를 저장하기 위해서는 등록된 유저 정보가 필요하다.")
	void saveSuccessTest() {
		// given
		given(seriesRepository.save(any(Series.class))).willReturn(series);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(series.getId()).willReturn(POST_ID);
		// when
		Long userId = seriesService.createSeries(createSeriesRequest, USER_ID);
		// then
		assertThat(userId).isEqualTo(1L);
		then(seriesRepository).should().save(any(Series.class));
		then(userRepository).should().findById(USER_ID);
	}

	@Test
	@DisplayName("등록된 유저가 없는 경우 시리즈를 만들때 예외가 발생한다.")
	void saveFailTest() {
		// given
		given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
		// when & then
		assertThatThrownBy(() -> seriesService.createSeries(createSeriesRequest, USER_ID))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("user");
	}

	@Test
	@DisplayName("시리즈 아이디로 시리즈를 찾을 수 있다.")
	void findByIdSuccessTest() {
		// given
		given(seriesRepository.joinPostFindByUserIdAndSeriesId(any(Long.class), any(Long.class)))
			.willReturn(Optional.of(series));
		given(series.getId()).willReturn(1L);
		given(series.getPosts()).willReturn(List.of());
		given(series.getTitle()).willReturn(SERIES_TITLE);
		// when
		SeriesResponse seriesResponse = seriesService.getSeriesById(1L, 1L);
		//then
		assertThat(seriesResponse)
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("title", SERIES_TITLE)
			.hasFieldOrPropertyWithValue("posts", List.of());
		then(seriesRepository).should().joinPostFindByUserIdAndSeriesId(1L, 1L);
	}

	@Test
	@DisplayName("존재하지 않는 시리즈 아이디로는 시리즈를 찾을 수 없다.")
	void findByIdFailBySeriesIdTest() {
		// given
		given(seriesRepository.joinPostFindByUserIdAndSeriesId(any(Long.class), any(Long.class)))
			.willReturn(Optional.empty());
		// when & then
		assertThatThrownBy(() -> seriesService.getSeriesById(1L, 1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("notExists");
	}
}