package com.prgrms.prolog.domain.like.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.like.dto.LikeDto;
import com.prgrms.prolog.domain.like.service.LikeService;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;

class LikeControllerTest extends ControllerTest {

	@Autowired
	LikeService likeService;

	@Autowired
	PostRepository postRepository;

	private Long userId;
	private Long postId;

	@BeforeEach
	void setPost() {
		Post post = Post.builder()
			.title(POST_TITLE)
			.content(POST_CONTENT)
			.openStatus(true)
			.user(savedUser)
			.build();
		Post savedPost = postRepository.save(post);
		postId = savedPost.getId();
		userId = savedUser.getId();
	}

	@Test
	void likeSaveApiTest() throws Exception {
		LikeDto.likeRequest likeRequest = new LikeDto.likeRequest(userId, postId);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/like/{postId}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(likeRequest)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("userId").description("사용자 아이디"),
					fieldWithPath("postId").description("게시물 아이디")
				),
				responseBody()
			));
	}

	@Test
	void likeCancelApiTest() throws Exception {
		LikeDto.likeRequest likeRequest = new LikeDto.likeRequest(userId, postId);
		likeService.save(likeRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/like")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(likeRequest)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("userId").description("사용자 아이디"),
					fieldWithPath("postId").description("게시물 아이디")
				),
				responseBody()
			));
	}
}