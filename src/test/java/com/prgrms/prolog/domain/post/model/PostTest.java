package com.prgrms.prolog.domain.post.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PostTest {

	@Test
	@DisplayName("게시글 생성")
	void createPostTest() {
		//given
		Post post = Post.builder()
			.title(POST_TITLE)
			.content(POST_CONTENT)
			.openStatus(true)
			.user(USER)
			.build();
		// when & then
		assertAll(
			() -> assertThat(post)
				.hasFieldOrPropertyWithValue("title", POST_TITLE)
				.hasFieldOrPropertyWithValue("content", POST_CONTENT)
				.hasFieldOrPropertyWithValue("openStatus", true),
			() -> assertThat(post.getUser()).isEqualTo(USER)
		);
	}

	@Test
	@DisplayName("게시글 수정")
	@Disabled
	void updatePostTest() {
		//given
		Post post = getPost();
		//when
		//then
		assertThat(post)
			.hasFieldOrPropertyWithValue("title", "수정된 제목")
			.hasFieldOrPropertyWithValue("content", "수정된 내용")
			.hasFieldOrPropertyWithValue("openStatus", false);
	}

	@Test
	@DisplayName("게시글을 생성하기 위해서는 사용자가 필요하다.")
	void createFailByUserNullTest() {
		//given & when & then
		assertThatThrownBy(() -> new Post(POST_TITLE, POST_CONTENT, true, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("게시글 제목은 50자를 넘을 수 없다.")
	void validateTitleTest() {
		//given & when & then
		assertThatThrownBy(() -> new Post(OVER_SIZE_50, POST_CONTENT, true, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글 제목은 빈 값,null일 수 없다.")
	void validateTitleTest2(String inputTitle) {
		//given & when & then
		assertThatThrownBy(() -> new Post(inputTitle, POST_CONTENT, true, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글 내용은 65535자를 넘을 수 없다.")
	void validateContentTest() {
		//given & when & then
		assertThatThrownBy(() -> new Post(POST_TITLE, OVER_SIZE_65535, true, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글 내용은 빈 값,null일 수 없다.")
	void validateContentTest2(String inputContent) {
		//given & when & then
		assertThatThrownBy(() -> new Post(POST_TITLE, inputContent, true, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글을 제목과 내용은 최대 글자 수가 넘어가도록 수정할 수 없다.")
	@Disabled
	void updatePostFailTest() {
		//given
		Post post = getPost();
		//when & then
		assertAll(
		);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글을 제목과 내용은 빈 값, null으로 변경할 수 없다.")
	@Disabled
	void updatePostFailTest2(String wrongInput) {
		//given
		Post post = getPost();
		//when & then
		assertAll(
		);
	}

}