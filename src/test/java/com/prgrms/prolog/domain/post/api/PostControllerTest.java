package com.prgrms.prolog.domain.post.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.post.dto.PostDto.CreatePostRequest;
import com.prgrms.prolog.domain.post.dto.PostDto.UpdatePostRequest;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;

class PostControllerTest extends ControllerTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private SeriesRepository seriesRepository;

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
		Series series = Series.builder()
			.title(SERIES_TITLE)
			.user(savedUser)
			.build();
		Series savedSeries = seriesRepository.save(series);
		savedPost.setSeries(savedSeries);
		postId = savedPost.getId();

	}

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save() throws Exception {
		CreatePostRequest request = new CreatePostRequest("생성된 테스트 제목", "생성된 테스트 내용", "tag", true, SERIES_TITLE);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			).andExpect(status().isCreated())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
					fieldWithPath("tagText").type(JsonFieldType.STRING).description("tagText"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("openStatus"),
					fieldWithPath("seriesTitle").type(JsonFieldType.STRING).description("seriesTitle")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("게시물을 전체 조회할 수 있다.")
	void findAll() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
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
					fieldWithPath("[].commentCount").type(JsonFieldType.NUMBER).description("commentCount"),
					fieldWithPath("[].seriesResponse").type(JsonFieldType.OBJECT).description("series"),
					fieldWithPath("[].seriesResponse.title").type(JsonFieldType.STRING).description("seriesTitle"),
					fieldWithPath("[].seriesResponse.posts").type(JsonFieldType.ARRAY).description("postInSeries"),
					fieldWithPath("[].seriesResponse.posts.[].id").type(JsonFieldType.NUMBER)
						.description("postIdInSeries"),
					fieldWithPath("[].seriesResponse.posts.[].title").type(JsonFieldType.STRING)
						.description("postTitleInSeries"),
					fieldWithPath("[].seriesResponse.count").type(JsonFieldType.NUMBER).description("seriesCount"),
					fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("likeCount")

				)));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 단건 조회할 수 있다.")
	void findById() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{id}", postId)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN))
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
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("commentCount"),
					fieldWithPath("seriesResponse").type(JsonFieldType.OBJECT).description("series"),
					fieldWithPath("seriesResponse.title").type(JsonFieldType.STRING).description("seriesTitle"),
					fieldWithPath("seriesResponse.posts").type(JsonFieldType.ARRAY).description("postInSeries"),
					fieldWithPath("seriesResponse.posts.[].id").type(JsonFieldType.NUMBER)
						.description("postIdInSeries"),
					fieldWithPath("seriesResponse.posts.[].title").type(JsonFieldType.STRING)
						.description("postTitleInSeries"),
					fieldWithPath("seriesResponse.count").type(JsonFieldType.NUMBER).description("seriesCount"),
					fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("likeCount")
				)));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 수정할 수 있다.")
	void update() throws Exception {
		UpdatePostRequest update = new UpdatePostRequest(UPDATE_TITLE, UPDATE_CONTENT, "", false);

		mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/posts/{id}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update))
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
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("commentCount"),
					fieldWithPath("seriesResponse").type(JsonFieldType.OBJECT).description("series"),
					fieldWithPath("seriesResponse.title").type(JsonFieldType.STRING).description("seriesTitle"),
					fieldWithPath("seriesResponse.posts").type(JsonFieldType.ARRAY).description("postInSeries"),
					fieldWithPath("seriesResponse.posts.[].id").type(JsonFieldType.NUMBER)
						.description("postIdInSeries"),
					fieldWithPath("seriesResponse.posts.[].title").type(JsonFieldType.STRING)
						.description("postTitleInSeries"),
					fieldWithPath("seriesResponse.count").type(JsonFieldType.NUMBER).description("seriesCount"),
					fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("likeCount")
				)
			));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 삭제할 수 있다.")
	void remove() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/posts/{id}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isNoContent())
			.andDo(document("post-delete"));
	}

	@Test
	@DisplayName("게시물 작성 중 제목이 공백인 경우 에러가 발생해야한다.")
	void validateTitleNull() throws Exception {
		CreatePostRequest createRequest = new CreatePostRequest("", "테스트 게시물 내용", "#tag", true, SERIES_TITLE);

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시물 작성 중 내용이 빈칸인 경우 에러가 발생해야한다.")
	void validateContentEmpty() throws Exception {
		CreatePostRequest createRequest = new CreatePostRequest("테스트 게시물 제목", " ", "#tag", true, SERIES_TITLE);
		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시물 작성 중 게시물 제목이 50이상인 경우 에러가 발생해야한다.")
	void validateTitleSizeOver() throws Exception {
		CreatePostRequest createRequest = new CreatePostRequest(
			"안녕하세요. 여기는 프로그래머스 기술 블로그 prolog입니다. 이곳에 글을 작성하기 위해서는 제목은 50글자 미만이어야합니다.",
			"null 게시물 내용", "#tag",
			true, SERIES_TITLE);

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("포스트 생성시 시리즈도 만들어진다.")
	void createSeries() throws Exception {
		CreatePostRequest createRequest = new CreatePostRequest(
			"안녕하세요. 여기는 프로그래머스 기술 블로그 prolog입니다",
			"null 게시물 내용", "#tag",
			true, "테스트 중");

		String requestJsonString = objectMapper.writeValueAsString(createRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/posts")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJsonString))
			.andExpect(status().isCreated());
	}
}