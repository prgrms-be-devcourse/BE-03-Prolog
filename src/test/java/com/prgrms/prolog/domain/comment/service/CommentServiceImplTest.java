// package com.prgrms.prolog.domain.comment.service;
//
// import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;
// import static com.prgrms.prolog.utils.TestUtils.*;
// import static org.assertj.core.api.AssertionsForClassTypes.*;
// import static org.mockito.Mockito.*;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
//
// import com.prgrms.prolog.base.ServiceTest;
//
// class CommentServiceImplTest extends ServiceTest {
//
// 	final CreateCommentRequest CREATE_COMMENT_REQUEST = new CreateCommentRequest(COMMENT.getContent());
// 	final UpdateCommentRequest UPDATE_COMMENT_REQUEST = new UpdateCommentRequest(COMMENT.getContent() + "updated");
// 	@Mock
// 	CommentServiceImpl commentService;
//
// 	@Test
// 	@DisplayName("댓글 저장에 성공한다.")
// 	void saveTest() {
// 		// given
// 		when(commentService.createComment(any(), anyLong(), anyLong())).thenReturn(1L);
// 		// when
// 		Long commentId = commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, 1L);
// 		// then
// 		assertThat(commentId).isEqualTo(1L);
// 	}
//
// 	@Test
// 	@DisplayName("댓글 수정에 성공한다.")
// 	void updateTest() {
// 		when(commentService.updateComment(any(), anyLong(), anyLong())).thenReturn(1L);
// 		Long commentId = commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, 1L);
// 		assertThat(commentId).isEqualTo(1L);
// 	}
//
// 	@Test
// 	@DisplayName("존재하지 않는 댓글을 수정하면 예외가 발생한다.")
// 	void updateNotExistsCommentThrowExceptionTest() {
// 		// given
// 		when(commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, 0L)).thenThrow(
// 			new IllegalArgumentException());
// 		// when & then
// 		assertThatThrownBy(() -> commentService.updateComment(UPDATE_COMMENT_REQUEST, USER_ID, 0L)).isInstanceOf(
// 			IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	@DisplayName("존재하지 않는 회원이 댓글을 저장하면 예외가 발생한다.")
// 	void updateCommentByNotExistsUserThrowExceptionTest() {
// 		// given
//
// 		final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("댓글 내용");
// 		when(commentService.updateComment(updateCommentRequest, UNSAVED_USER_ID, 1L)).thenThrow(
// 			new IllegalArgumentException());
// 		// when & then
// 		assertThatThrownBy(() -> commentService.updateComment(updateCommentRequest, UNSAVED_USER_ID, 1L)).isInstanceOf(
// 			IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	@DisplayName("존재하지 않는 게시글에 댓글을 저장하면 예외가 발생한다.")
// 	void saveCommentNotExistsPostThrowExceptionTest() {
// 		// given
// 		when(commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, 0L)).thenThrow(
// 			new IllegalArgumentException());
// 		// when & then
// 		assertThatThrownBy(() -> commentService.createComment(CREATE_COMMENT_REQUEST, USER_ID, 0L)).isInstanceOf(
// 			IllegalArgumentException.class);
// 	}
//
// }