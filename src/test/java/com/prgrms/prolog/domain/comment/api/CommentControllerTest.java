package com.prgrms.prolog.domain.comment.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.comment.dto.CommentDto;
import com.prgrms.prolog.domain.comment.service.CommentService;
import com.prgrms.prolog.utils.TestUtils;

class CommentControllerTest extends ControllerTest {

	@MockBean
	CommentService commentService;

	@Test
	void commentSaveApiTest() throws Exception {
		CommentDto.CreateCommentRequest createCommentRequest = new CommentDto.CreateCommentRequest(
			TestUtils.getComment().getContent());

		when(commentService.save(createCommentRequest, savedUserId, 1L))
			.thenReturn(1L);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts/{post_id}/comments", 1L)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCommentRequest)))
			.andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("content").description("댓글 내용")
				),
				responseBody()
			));
	}

	@Test
	void commentUpdateApiTest() throws Exception {
		CommentDto.UpdateCommentRequest updateCommentRequest = new CommentDto.UpdateCommentRequest(
			TestUtils.getComment().getContent() + "updated");

		when(commentService.update(updateCommentRequest, savedUserId, 1L))
			.thenReturn(1L);

		// when
		mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/posts/{post_id}/comments/{id}", 1, 1)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateCommentRequest)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("content").description("수정된 댓글 내용")
				),
				responseBody()
			));
	}
}