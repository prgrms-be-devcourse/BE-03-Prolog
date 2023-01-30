package com.prgrms.prolog.domain.usertag.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTagTest {

	@Test
	@DisplayName("유저 태그 생성 성공")
	void createUserTagTest() {
		// given
		UserTag userTag = UserTag.builder()
			.user(USER)
			.rootTag(ROOT_TAG)
			.count(1)
			.build();
		// when & then
		assertThat(userTag)
			.hasFieldOrPropertyWithValue("user", USER)
			.hasFieldOrPropertyWithValue("rootTag", ROOT_TAG);
	}

	@Test
	@DisplayName("유저 태그에는 유저와 루트 태그가 null일 수 없다.")
	void validateUserTagNulLTest() {
		assertAll(
			() -> assertThatThrownBy(() -> new UserTag(USER, null))
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> new UserTag(null, ROOT_TAG))
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> new UserTag(null, null))
				.isInstanceOf(IllegalArgumentException.class)
		);
	}
}