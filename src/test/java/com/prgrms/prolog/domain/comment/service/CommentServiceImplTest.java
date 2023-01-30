package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.prgrms.prolog.base.ServiceTest;

class CommentServiceImplTest extends ServiceTest {

	final CreateCommentRequest CREATE_COMMENT_REQUEST = new CreateCommentRequest(COMMENT.getContent());
	final UpdateCommentRequest UPDATE_COMMENT_REQUEST = new UpdateCommentRequest(COMMENT.getContent() + "updated");
	final SingleCommentResponse SINGLE_COMMENT_RESPONSE = new SingleCommentResponse(USER_PROFILE, COMMENT.getContent());
	final SingleCommentResponse UPDATED_SINGLE_COMMENT_RESPONSE
		= new SingleCommentResponse(USER_PROFILE, COMMENT.getContent() + "updated");

	@Mock
	CommentServiceImpl commentService;

	@Test
	@DisplayName("댓글 저장에 성공한다.")
	void saveTest() {
		// given
		when(commentService.createComment(any(), anyLong(), anyLong())).thenReturn(SINGLE_COMMENT_RESPONSE);
		// when
		SingleCommentResponse singleCommentResponse
			= commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, POST_ID);
		// then
		assertThat(singleCommentResponse)
			.hasFieldOrPropertyWithValue("user", USER_PROFILE)
			.hasFieldOrPropertyWithValue("content", COMMENT.getContent());
	}

	@Test
	@DisplayName("댓글 수정에 성공한다.")
	void updateTest() {
		when(commentService.updateComment(any(), anyLong(), anyLong(), anyLong())).thenReturn(UPDATED_SINGLE_COMMENT_RESPONSE);
		SingleCommentResponse singleCommentResponse
			= commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, POST_ID, COMMENT_ID);
		assertThat(singleCommentResponse)
			.hasFieldOrPropertyWithValue("user", USER_PROFILE)
			.hasFieldOrPropertyWithValue("content", UPDATED_SINGLE_COMMENT_RESPONSE.content());
	}

	@Test
	@DisplayName("존재하지 않는 댓글을 수정하면 예외가 발생한다.")
	void updateNotExistsCommentThrowExceptionTest() {
		// given
		when(commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, POST_ID, COMMENT_ID))
			.thenThrow(new IllegalArgumentException());
		// when & then
		assertThatThrownBy(() -> commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, POST_ID, COMMENT_ID))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하지 않는 회원이 댓글을 저장하면 예외가 발생한다.")
	void updateCommentByNotExistsUserThrowExceptionTest() {
		// given
		final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("댓글 내용");
		// when
		when(commentService.updateComment(updateCommentRequest, UNSAVED_USER_ID, POST_ID, COMMENT_ID))
			.thenThrow(new IllegalArgumentException());
		// then
		assertThatThrownBy(
			() -> commentService.updateComment(updateCommentRequest, UNSAVED_USER_ID, POST_ID, COMMENT_ID))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하지 않는 게시글에 댓글을 저장하면 예외가 발생한다.")
	void saveCommentNotExistsPostThrowExceptionTest() {
		// given
		when(commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, 0L))
			.thenThrow(new IllegalArgumentException());
		// when & then
		assertThatThrownBy(() -> commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, 0L))
			.isInstanceOf(IllegalArgumentException.class);
	}

}