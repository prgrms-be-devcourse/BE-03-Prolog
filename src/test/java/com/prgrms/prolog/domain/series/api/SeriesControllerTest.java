package com.prgrms.prolog.domain.series.api;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;

class SeriesControllerTest extends ControllerTest {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private SeriesRepository seriesRepository;

	private Post savedPost;
	private Series savedSeries;

	@BeforeEach
	void setUpSeries() {
		Post post = Post.builder()
			.title(POST_TITLE)
			.content(POST_CONTENT)
			.openStatus(true)
			.user(savedUser)
			.build();
		savedPost = postRepository.save(post);
		Series series = Series.builder()
			.title(SERIES_TITLE)
			.user(savedUser)
			.post(savedPost)
			.build();
		savedSeries = seriesRepository.save(series);
	}

	final CreateSeriesRequest createSeriesRequest = CreateSeriesRequest.from("새로운 시리즈");
	final UpdateSeriesRequest updateSeriesRequest = UpdateSeriesRequest.from("수정된 시리즈");

	@Test
	@DisplayName("새로운 시리즈를 생성할 수 있다.")
	void createNewSeriesTest() throws Exception {
		// when
		mockMvc.perform(post("/api/v1/user/series")
				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createSeriesRequest))
			)
			// then
			.andExpect(status().isCreated())
			// docs
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("title").description("시리즈 제목")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("해당 시리즈의 게시글들을 가져올 수 있다.")
	void getSingleSeriesTest() throws Exception {
		// when
		mockMvc.perform(get("/api/v1/user/{userId}/series/{seriesId}", savedUserId,
				savedSeries.getId())
				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpect(status().isOk())
			.andDo(print())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("id").description("시리즈 번호"),
					fieldWithPath("title").type(STRING).description("시리즈 제목"),
					fieldWithPath("posts").type(ARRAY).description("게시글 목록"),
					fieldWithPath("posts.[].id").type(NUMBER).description("게시글 아이디"),
					fieldWithPath("posts.[].title").type(STRING).description("게시글 제목"),
					fieldWithPath("count").type(NUMBER).description("게시물 개수")
				))
			);
	}

	@Test
	@DisplayName("유저의 아이디로 시리즈 목록을 가져올 수 있다.")
	void getAllSeriesTest() throws Exception {
		// when
		mockMvc.perform(get("/api/v1/user/{userId}/series", savedUserId)
				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpect(
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("[].id").type(NUMBER).description("시리즈 번호"),
					fieldWithPath("[].title").description("시리즈 제목"),
					fieldWithPath("[].count").type(NUMBER).description("시리즈 개수")
				))
			);
	}

	@Test
	@DisplayName("유저는 자신의 시리즈를 수정할 수 있다.")
	void updateSeriesTest() throws Exception {

		System.out.println("rnjswntjdxmfhf");
		// when
		mockMvc.perform(put("/api/v1/user/series/{seriesId}", savedSeries.getId())
				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateSeriesRequest))
			)
			// then
			.andExpect(
				status().isOk())
			// docs
			.andDo(print())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("id").type(NUMBER).description("시리즈 번호"),
					fieldWithPath("title").type(STRING).description("시리즈 제목"),
					fieldWithPath("posts").type(ARRAY).description("게시글 목록"),
					fieldWithPath("posts.[].id").type(NUMBER).description("게시글 아이디"),
					fieldWithPath("posts.[].title").type(STRING).description("게시글 제목"),
					fieldWithPath("count").type(NUMBER).description("게시물 개수")
				))
			);
	}

	@Test
	@DisplayName("유저는 자신의 시리즈를 삭제할 수 있다.")
	void deleteSeriesTest() throws Exception {
		// when
		mockMvc.perform(delete("/api/v1/user/series/{seriesId}", savedSeries.getId())
				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpect(
				status().isNoContent())
			// docs
			.andDo(restDocs.document(
				responseBody()
			));
	}
}
