package com.prgrms.prolog.domain.roottag.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RootTagTest {

	@Test
	@DisplayName("태그 생성")
	void createRootTagTest() {
		// given
		RootTag rootTag = RootTag.builder()
			.name(ROOT_TAG_NAME)
			.build();
		// when & then
		assertThat(rootTag).hasFieldOrPropertyWithValue("name", ROOT_TAG_NAME);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("태그 이름은 null, 빈 값일 수 없다.")
	void validateRootTagNameTextTest(String name) {
		// given & when & then
		assertAll(
			() -> assertThatThrownBy(() -> RootTag.builder().name(name).build())
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> new RootTag(name))
				.isInstanceOf(IllegalArgumentException.class)
		);
	}

	@Test
	@DisplayName("태그 이름은 100글자 이내여야 한다.")
	void validateRootTagNameLengthTest() {
		// given & when & then
		assertThatThrownBy(() -> new RootTag(OVER_SIZE_100))
			.isInstanceOf(IllegalArgumentException.class);
	}
}