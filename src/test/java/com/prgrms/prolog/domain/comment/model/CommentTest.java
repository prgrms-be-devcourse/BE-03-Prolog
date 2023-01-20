package com.prgrms.prolog.domain.comment.model;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class CommentTest {

	private static final String content = "댓글 내용";

	@Test
	@DisplayName("댓글 생성")
	void createTest() {
		//given
		Comment comment = Comment.builder()
			.content(content)
			.post(POST)
			.user(USER)
			.build();
		//when & then
		assertAll(
			() -> assertThat(comment)
				.hasFieldOrPropertyWithValue("content", content),
			() -> assertThat(comment.getPost()).isEqualTo(POST)
		);

	}

	@Test
	@DisplayName("댓글을 생성하기 위해서는 게시글이 필요하다")
	void createFailByPostNullTest() {
		//given & when & then
		assertThatThrownBy(() -> new Comment(content, null, USER))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("댓글을 생성하기 위해서는 유저가 필요하다")
	void createFailByUserNullTest() {
		//given & when & then
		assertThatThrownBy(() -> new Comment(content, POST, null))
			.isInstanceOf(NullPointerException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("댓글 내용은 null,빈 값일 수 없다.")
	void validateContentTest(String inputContent) {
		//given & when & then
		assertThatThrownBy(() -> new Comment(inputContent, POST, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("댓글은 최대 255자 이내에 작성되어야 한다.")
	void validateContentLengthTest() {
		//given & when & then
		assertThatThrownBy(() -> new Comment(OVER_SIZE_255, POST, USER))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("댓글을 수정할 때도 255자 이내여야 한다.")
	void validateContentLengthInChangeContentTest() {
		//given
		Comment comment = getComment();
		//when & then
		assertThatThrownBy(() -> comment.changeContent(OVER_SIZE_255))
			.isInstanceOf(IllegalArgumentException.class);
	}
}