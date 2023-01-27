package com.prgrms.prolog.domain.like.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.domain.like.dto.LikeDto;
import com.prgrms.prolog.domain.like.service.LikeService;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;
import com.prgrms.prolog.global.jwt.JwtTokenProvider.Claims;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@Transactional
class LikeControllerTest {

	@Autowired
	RestDocumentationResultHandler restDocs;

	@Autowired
	LikeService likeService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	MockMvc mockMvc;

	@BeforeEach
	void setUpRestDocs(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(restDocs)
			.apply(springSecurity())
			.build();
	}

	@Test
	void likeSaveApiTest() throws Exception {
		User savedUser = userRepository.save(USER);
		Post post = getPost();
		post.setUser(savedUser);
		Post savedPost = postRepository.save(post);
		Claims claims = Claims.from(savedUser.getId(), USER_ROLE);

		LikeDto.likeRequest likeRequest = new LikeDto.likeRequest(savedUser.getId(), savedPost.getId());

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/like")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(likeRequest)))
			.andExpect(status().isCreated())
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
		User savedUser = userRepository.save(USER);
		Post post = getPost();
		post.setUser(savedUser);
		Post savedPost = postRepository.save(post);

		Claims claims = Claims.from(savedUser.getId(), USER_ROLE);

		LikeDto.likeRequest likeRequest = new LikeDto.likeRequest(savedUser.getId(), savedPost.getId());
		likeService.save(likeRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/like")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
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