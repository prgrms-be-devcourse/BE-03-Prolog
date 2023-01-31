package com.prgrms.prolog.domain.comment.api;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;
import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.comment.service.CommentService;

class CommentControllerTest extends ControllerTest {

	UserResponse USER_RESPONSE;
	CreateCommentRequest CREATE_COMMENT_REQUEST;
	UpdateCommentRequest UPDATE_COMMENT_REQUEST;
	SingleCommentResponse SINGLE_COMMENT_RESPONSE;
	SingleCommentResponse UPDATED_SINGLE_COMMENT_RESPONSE;

	@MockBean
	CommentService commentService;

	@BeforeEach
	void setup() {
		USER_RESPONSE = UserResponse.from(savedUser);
		CREATE_COMMENT_REQUEST = CreateCommentRequest.from(COMMENT.getContent());
		UPDATE_COMMENT_REQUEST = UpdateCommentRequest.from(COMMENT.getContent() + "updated");
		SINGLE_COMMENT_RESPONSE = SingleCommentResponse.from(USER_RESPONSE, COMMENT.getContent());
		UPDATED_SINGLE_COMMENT_RESPONSE = SingleCommentResponse.from(USER_RESPONSE, COMMENT.getContent() + "updated");
	}

	@Test
	void commentSaveApiTest() throws Exception {
		when(commentService.createComment(any(), anyLong(), anyLong())).thenReturn(SINGLE_COMMENT_RESPONSE);

		mockMvc.perform(post("/api/v1/posts/{postId}/comments", POST_ID).header(HttpHeaders.AUTHORIZATION,
					BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(CREATE_COMMENT_REQUEST)))
			.andExpect(status().isCreated())
			.andDo(restDocs.document(requestFields(fieldWithPath("content").description("댓글 내용")),
				responseFields(fieldWithPath("user.userId").description("댓글 작성자 회원 번호"),
					fieldWithPath("user.nickName").description("댓글 작성자 회원 닉네임"),
					fieldWithPath("user.profileImgUrl").description("댓글 작성자 프로파일 이미지"),
					fieldWithPath("content").description("댓글 내용"))));
	}

	@Test
	void commentUpdateApiTest() throws Exception {
		when(commentService.updateComment(any(), anyLong(), anyLong(), anyLong())).thenReturn(
			UPDATED_SINGLE_COMMENT_RESPONSE);

		mockMvc.perform(
				patch("/api/v1/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID).header(HttpHeaders.AUTHORIZATION,
						BEARER_TYPE + ACCESS_TOKEN)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(UPDATE_COMMENT_REQUEST)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(requestFields(fieldWithPath("content").description("수정된 댓글 내용")),
				responseFields(fieldWithPath("user.userId").description("댓글 작성자 회원 번호"),
					fieldWithPath("user.nickName").description("댓글 작성자 회원 닉네임"),
					fieldWithPath("user.profileImgUrl").description("댓글 작성자 프로파일 이미지"),
					fieldWithPath("content").description("댓글 내용"))));
	}
}