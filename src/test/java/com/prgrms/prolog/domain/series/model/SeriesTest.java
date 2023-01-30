package com.prgrms.prolog.domain.series.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.utils.TestUtils;

class SeriesTest {

	@Test
	@DisplayName("시리즈를 생성할 수 있다.")
	void createSuccessTest(){
	    // given & when & then
		assertDoesNotThrow(
			() -> Series.builder()
				.title(TestUtils.SERIES_TITLE)
				.user(USER)
				.post(POST)
				.build()
		);
	}

	@Test
	@DisplayName("시리즈와 연관된 엔티티를 조회할 수 있다.")
	void readTest(){
		// given & when & then
		Series series = getSeries();
		assertAll(
			() -> assertThat(series.getTitle()).isEqualTo(TestUtils.SERIES_TITLE),
			() -> assertThat(series.getUser()).isEqualTo(USER),
			() -> assertThat(series.getPosts()).isEqualTo(List.of(POST))
		);
	}

	@Test
	@DisplayName("시리즈는 포스트 없이도 생성할 수 있다.")
	void createSuccessDoesNotExistPostTest(){
		// given & when & then
		assertDoesNotThrow(
			() -> Series.builder()
				.title(TestUtils.SERIES_TITLE)
				.user(USER)
				.build()
		);
	}

	@Test
	@DisplayName("시리즈는 유저 없이 생성할 수 없다.")
	void createFailTest(){
		// given & when & then
		assertThatThrownBy(
			() -> Series.builder()
				.title(TestUtils.SERIES_TITLE)
				.post(POST)
				.build()
		)
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("user");
	}

}