package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.prgrms.prolog.base.ServiceTest;
import com.prgrms.prolog.domain.post.dto.PostInfo;
import com.prgrms.prolog.domain.series.dto.CreateSeriesRequest;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.global.common.IdResponse;

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
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(series.getId()).willReturn(POST_ID);
		// when
		IdResponse response = seriesService.createSeries(createSeriesRequest, USER_ID);
		// then
		assertThat(response.id()).isEqualTo(POST_ID);
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
	@DisplayName("등록된 유저가 없는 경우 시리즈를 만들때 예외가 발생한다.")
	void findByTitleSuccessTest() {
		// given
		given(seriesRepository.findByIdAndTitle(any(Long.class), any(String.class)))
			.willReturn(Optional.of(series));
		given(series.getTitle()).willReturn(SERIES_TITLE);
		given(series.getPosts()).willReturn((List.of(post)));
		given(post.getId()).willReturn(POST_ID);
		given(post.getTitle()).willReturn(POST_TITLE);
		// when
		SeriesResponse seriesResponse = seriesService.findSeriesByTitle(USER_ID, SERIES_TITLE);
		// then
		then(seriesRepository).should().findByIdAndTitle(any(Long.class), any(String.class));
		assertThat(seriesResponse)
			.hasFieldOrPropertyWithValue("title", SERIES_TITLE)
			.hasFieldOrPropertyWithValue("posts", List.of(new PostInfo(1L, POST_TITLE)))
			.hasFieldOrPropertyWithValue("count", 1);
		assertThat(seriesResponse.posts()).isNotEmpty();
	}

	@Test
	@DisplayName("찾는 시리즈가 없으면 예외가 발생한다.")
	void findByTitleFailTest() {
		// given
		given(seriesRepository.findByIdAndTitle(any(Long.class), any(String.class)))
			.willReturn(Optional.empty());
		// when & then
		assertThatThrownBy(() -> seriesService.findSeriesByTitle(USER_ID, SERIES_TITLE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("notExists");
	}
}