package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.prolog.domain.post.dto.PostInfo;
import com.prgrms.prolog.domain.series.dto.CreateSeriesRequest;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.common.IdResponse;

@ExtendWith(MockitoExtension.class)
class SeriesServiceImplTest {

	@Mock
	private SeriesRepository seriesRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Series series;

	@InjectMocks
	private SeriesServiceImpl seriesService;

	@Test
	@DisplayName("시리즈를 저장하기 위해서는 등록된 유저 정보가 필요하다.")
	void saveSuccessTest() {
		// given
		CreateSeriesRequest createSeriesRequest
			= new CreateSeriesRequest(SERIES_TITLE);
		given(seriesRepository.save(any(Series.class))).willReturn(series);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(series.getId()).willReturn(1L);
		// when
		IdResponse response = seriesService.create(createSeriesRequest, USER_ID);
		// then
		assertThat(response.id()).isEqualTo(1L);
		then(seriesRepository).should().save(any(Series.class));
		then(userRepository).should().findById(USER_ID);
	}

	@Test
	@DisplayName("등록된 유저가 없는 경우 시리즈를 만들때 예외가 발생한다.")
	void saveFailTest() {
		// given
		CreateSeriesRequest createSeriesRequest
			= new CreateSeriesRequest(SERIES_TITLE);
		given(userRepository.findById(USER_ID)).willReturn(Optional.empty());
		// when & then
		assertThatThrownBy(() -> seriesService.create(createSeriesRequest, USER_ID))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("user");
	}

	@Test
	@DisplayName("등록된 유저가 없는 경우 시리즈를 만들때 예외가 발생한다.")
	void findByTitleSuccessTest() {
		// given
		given(seriesRepository.findByIdAndTitle(any(Long.class),any(String.class)))
			.willReturn(Optional.of(series));
		given(series.getTitle()).willReturn(SERIES_TITLE);
		given(series.getPosts()).willReturn((List.of(POST)));
		// when
		SeriesResponse seriesResponse = seriesService.findByTitle(USER_ID, SERIES_TITLE);
		// then
		then(seriesRepository).should().findByIdAndTitle(any(Long.class),any(String.class));
		assertThat(seriesResponse)
			.hasFieldOrPropertyWithValue("title", SERIES_TITLE)
			.hasFieldOrPropertyWithValue("posts", List.of(new PostInfo(POST_TITLE,POST_CONTENT)))
			.hasFieldOrPropertyWithValue("count",1);
		assertThat(seriesResponse.posts()).isNotEmpty();
	}

	@Test
	@DisplayName("찾는 시리즈가 없으면 예외가 발생한다.")
	void findByTitleFailTest() {
		// given
		given(seriesRepository.findByIdAndTitle(any(Long.class),any(String.class)))
			.willReturn(Optional.empty());
		// when & then
		assertThatThrownBy(() -> seriesService.findByTitle(USER_ID, SERIES_TITLE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("notExists");
	}
}