package com.prgrms.prolog.domain.post.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PostTest {

	private static final String title = "제목";
	private static final String content = "내용";

	@Test
	@DisplayName("게시글 생성")
	void createPostTest() {
		//given
		Post post = Post.builder()
			.title(title)
			.content(content)
			.openStatus(true)
			.user(USER)
			.build();
		// when & then
		assertAll(
			() -> assertThat(post)
				.hasFieldOrPropertyWithValue("title", title)
				.hasFieldOrPropertyWithValue("content", content)
				.hasFieldOrPropertyWithValue("openStatus", true),
			() -> assertThat(post.getUser()).isEqualTo(USER)
		);
	}

	@Test
	@DisplayName("게시글 수정")
	void updatePostTest() {
		//given
		Post post = getPost();
		//when
		post.changeTitle("수정된 제목");
		post.changeContent("수정된 내용");
		post.changeOpenStatus(false);
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
		assertThatThrownBy(() -> new Post(title, content, true, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("게시글");
	}

	@Test
	@DisplayName("게시글 제목은 50자를 넘을 수 없다.")
	void validateTitleTest() {
		//given & when & then
		assertThatThrownBy(() -> new Post(OVER_SIZE_50, content, true, USER))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("초과");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글 제목은 빈 값,null일 수 없다.")
	void validateTitleTest2(String inputTitle) {
		//given & when & then
		assertThatThrownBy(() -> new Post(inputTitle, content, true, USER))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈 값");
	}

	@Test
	@DisplayName("게시글 내용은 65535자를 넘을 수 없다.")
	void validateContentTest() {
		//given & when & then
		assertThatThrownBy(() -> new Post(title, OVER_SIZE_65535, true, USER))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("초과");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글 내용은 빈 값,null일 수 없다.")
	void validateContentTest2(String inputContent) {
		//given & when & then
		assertThatThrownBy(() -> new Post(title, inputContent, true, USER))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈 값");
	}

	@Test
	@DisplayName("게시글을 제목과 내용은 최대 글자 수가 넘어가도록 수정할 수 없다.")
	void updatePostFailTest() {
		//given
		Post post = getPost();
		//when & then
		assertAll(
			() -> assertThatThrownBy(() -> post.changeTitle(OVER_SIZE_50)),
			() -> assertThatThrownBy(() -> post.changeContent(OVER_SIZE_65535))
		);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("게시글을 제목과 내용은 빈 값, null으로 변경할 수 없다.")
	void updatePostFailTest2(String wrongInput) {
		//given
		Post post = getPost();
		//when & then
		assertAll(
			() -> assertThatThrownBy(() -> post.changeTitle(wrongInput)),
			() -> assertThatThrownBy(() -> post.changeContent(wrongInput))
		);
	}

}