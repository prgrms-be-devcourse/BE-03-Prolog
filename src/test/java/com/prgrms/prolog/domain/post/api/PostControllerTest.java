package com.prgrms.prolog.domain.post.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.config.TestContainerConfig;
import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.service.PostServiceImpl;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;
import com.prgrms.prolog.global.jwt.JwtTokenProvider.Claims;

@ExtendWith(RestDocumentationExtension.class)
@Import({RestDocsConfig.class, TestContainerConfig.class})
@SpringBootTest
@Transactional
class PostControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	RestDocumentationResultHandler restDocs;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PostServiceImpl postService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SeriesRepository seriesRepository;
	Long postId;
	Long userId;
	Claims claims;


	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {

		userId = userRepository.save(USER).getId();
		claims = Claims.from(userId, "ROLE_USER");
		CreateRequest createRequest = new CreateRequest("테스트 제목", "테스트 내용", "#tag", false,null);
		postId = postService.save(createRequest, userId);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(restDocs)
			.apply(springSecurity())
			.build();
	}

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save() throws Exception {
		CreateRequest request = new CreateRequest("생성된 테스트 제목", "생성된 테스트 내용", "tag", true, null);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			).andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("tagText").type(JsonFieldType.STRING).description("tagText"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("openStatus")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("게시물을 전체 조회할 수 있다.")
	void findAll() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims)))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("[].title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("[].content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("[].openStatus").type(JsonFieldType.BOOLEAN).description("openStatus"),
					fieldWithPath("[].user.id").type(JsonFieldType.NUMBER).description("userId"),
					fieldWithPath("[].user.email").type(JsonFieldType.STRING).description("userEmail"),
					fieldWithPath("[].user.nickName").type(JsonFieldType.STRING).description("nickName"),
					fieldWithPath("[].user.introduce").type(JsonFieldType.STRING).description("introduce"),
					fieldWithPath("[].user.prologName").type(JsonFieldType.STRING).description("prologName"),
					fieldWithPath("[].user.profileImgUrl").type(JsonFieldType.STRING).description("profileImgUrl"),
					fieldWithPath("[].tags").type(JsonFieldType.ARRAY).description("tags"),
					fieldWithPath("[].comment").type(JsonFieldType.ARRAY).description("comment"),
					fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("commentCount")
				)));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 단건 조회할 수 있다.")
	void findById() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{id}", postId)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("openStatus"),
					fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("userId"),
					fieldWithPath("user.email").type(JsonFieldType.STRING).description("userEmail"),
					fieldWithPath("user.nickName").type(JsonFieldType.STRING).description("nickName"),
					fieldWithPath("user.introduce").type(JsonFieldType.STRING).description("introduce"),
					fieldWithPath("user.prologName").type(JsonFieldType.STRING).description("prologName"),
					fieldWithPath("user.profileImgUrl").type(JsonFieldType.STRING).description("profileImgUrl"),
					fieldWithPath("tags").type(JsonFieldType.ARRAY).description("tags"),
					fieldWithPath("comment").type(JsonFieldType.ARRAY).description("comment"),
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("commentCount")
				)));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 수정할 수 있다.")
	void update() throws Exception {
		UpdateRequest request = new UpdateRequest("수정된 테스트 제목", "수정된 테스트 내용", "", true);

		mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/posts/{id}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			).andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("tagText").type(JsonFieldType.STRING).description("tagText"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("openStatus")
				),
				responseFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("openStatus"),
					fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("userId"),
					fieldWithPath("user.email").type(JsonFieldType.STRING).description("userEmail"),
					fieldWithPath("user.nickName").type(JsonFieldType.STRING).description("nickName"),
					fieldWithPath("user.introduce").type(JsonFieldType.STRING).description("introduce"),
					fieldWithPath("user.prologName").type(JsonFieldType.STRING).description("prologName"),
					fieldWithPath("user.profileImgUrl").type(JsonFieldType.STRING).description("profileImgUrl"),
					fieldWithPath("tags").type(JsonFieldType.ARRAY).description("tags"),
					fieldWithPath("comment").type(JsonFieldType.ARRAY).description("comment"),
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("commentCount")
				)
			));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 삭제할 수 있다.")
	void remove() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/posts/{id}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isNoContent())
			.andDo(document("post-delete"));
	}

	@Test
	@DisplayName("게시물 작성 중 제목이 공백인 경우 에러가 발생해야한다.")
	void isValidateTitleNull() throws Exception {
		CreateRequest createRequest = new CreateRequest("", "테스트 게시물 내용", "#tag", true, null);

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시물 작성 중 내용이 빈칸인 경우 에러가 발생해야한다.")
	void isValidateContentEmpty() throws Exception {
		CreateRequest createRequest = new CreateRequest("테스트 게시물 제목", " ", "#tag", true, null);

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시물 작성 중 게시물 제목이 50이상인 경우 에러가 발생해야한다.")
	void isValidateTitleSizeOver() throws Exception {
		CreateRequest createRequest = new CreateRequest(
			"안녕하세요. 여기는 프로그래머스 기술 블로그 prolog입니다. 이곳에 글을 작성하기 위해서는 제목은 50글자 미만이어야합니다.",
			"null 게시물 내용", "#tag",
			true, null);

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("포스트 생성시 시리즈도 만들어진다.")
	void createSeries() throws Exception {
		CreateRequest createRequest = new CreateRequest(
			"안녕하세요. 여기는 프로그래머스 기술 블로그 prolog입니다",
			"null 게시물 내용", "#tag",
			true, "테스트 중");

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isCreated());
	}
}