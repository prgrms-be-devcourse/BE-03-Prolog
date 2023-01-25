package com.prgrms.prolog.domain.posttag.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTagTest {

	@Test
	@DisplayName("게시글 태그 생성")
	void createPostTagTest() {
		// given
		PostTag postTag = PostTag.builder()
			.rootTag(ROOT_TAG)
			.post(POST)
			.build();
		// when & then
		assertThat(postTag)
			.hasFieldOrPropertyWithValue("rootTag", ROOT_TAG)
			.hasFieldOrPropertyWithValue("post", POST);
	}

	@Test
	@DisplayName("게시글 태그에는 게시글과 루트 태그가 null일 수 없다.")
	void validatePostTagNullTest() {
		assertAll(
			() -> assertThatThrownBy(() -> new PostTag(null, ROOT_TAG))
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> new PostTag(POST, null))
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> new PostTag(null, null))
				.isInstanceOf(IllegalArgumentException.class)
		);
	}

}