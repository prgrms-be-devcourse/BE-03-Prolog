package com.prgrms.prolog.domain.post.api;

import static com.prgrms.prolog.utils.TestUtils.*;
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
					fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시물의 내용"),
					fieldWithPath("tagText").type(JsonFieldType.STRING).description("게시물의 요청 태그"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("게시물의 공개 여부"),
					fieldWithPath("seriesTitle").type(JsonFieldType.STRING).description("게시물이 저장된 시리즈의 제목")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("메인 페이지에서 전체 게시물을 조회할 수 있다.")
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
					fieldWithPath("stock.[].title").type(JsonFieldType.STRING).description("게시물의 제목"),
					fieldWithPath("stock.[].content").type(JsonFieldType.STRING).description("게시물의 내용"),
					fieldWithPath("stock.[].openStatus").type(JsonFieldType.BOOLEAN).description("게시물의 공개 여부"),
					fieldWithPath("stock.[].user.id").type(JsonFieldType.NUMBER).description("게시물을 작성한 사용자의 아이디"),
					fieldWithPath("stock.[].user.email").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 이메일"),
					fieldWithPath("stock.[].user.nickName").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 닉네임"),
					fieldWithPath("stock.[].user.introduce").type(JsonFieldType.STRING)
						.description("게시물을 작성한 사용자의 한 줄 소개"),
					fieldWithPath("stock.[].user.prologName").type(JsonFieldType.STRING)
						.description("게시물을 작성한 사용자의 블로그명"),
					fieldWithPath("stock.[].user.profileImgUrl").type(JsonFieldType.STRING)
						.description("게시물을 작성한 사용자의 프로필 이미지 주소"),
					fieldWithPath("stock.[].tags").type(JsonFieldType.ARRAY).description("게시물의 태그"),
					fieldWithPath("stock.[].series").type(JsonFieldType.OBJECT).description("시리즈"),
					fieldWithPath("stock.[].series.title").type(JsonFieldType.STRING).description("시리즈의 이름"),
					fieldWithPath("stock.[].series.posts").type(JsonFieldType.ARRAY).description("시리즈에 포함된 게시물"),
					fieldWithPath("stock.[].series.posts.[].id").type(JsonFieldType.NUMBER)
						.description("시리즈에 포함된 게시물의 아이디"),
					fieldWithPath("stock.[].series.posts.[].title").type(JsonFieldType.STRING)
						.description("시리즈에 포함된 게시물의 제목"),
					fieldWithPath("stock.[].series.count").type(JsonFieldType.NUMBER).description("시리즈에 포함된 게시물의 개수"),
					fieldWithPath("stock.[].comment").type(JsonFieldType.ARRAY).description("게시물의 댓글"),
					fieldWithPath("stock.[].commentCount").type(JsonFieldType.NUMBER).description("게시물의 댓글 수"),
					fieldWithPath("stock.[].likeCount").type(JsonFieldType.NUMBER).description("게시물의 좋아요 수"),
					fieldWithPath("pageableCustom.first").type(JsonFieldType.BOOLEAN).description("현재 페이지가 첫 페이지"),
					fieldWithPath("pageableCustom.last").type(JsonFieldType.BOOLEAN).description("현재 페이지가 마지막 페이지"),
					fieldWithPath("pageableCustom.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
					fieldWithPath("pageableCustom.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
					fieldWithPath("pageableCustom.totalElements").type(JsonFieldType.NUMBER)
						.description("모든 페이지에 존재하는 총 게시물 수"),
					fieldWithPath("pageableCustom.page").type(JsonFieldType.NUMBER).description("검색을 원하는 페이지 번호"),
					fieldWithPath("pageableCustom.size").type(JsonFieldType.NUMBER).description("한 페이지에 보이는 게시물 개수"),
					fieldWithPath("pageableCustom.sort").type(JsonFieldType.OBJECT).description("페이지 정렬 방식"),
					fieldWithPath("pageableCustom.sort.sorted").type(JsonFieldType.BOOLEAN)
						.description("페이지 정렬 방식 오름차순인 경우"),
					fieldWithPath("pageableCustom.sort.unsorted").type(JsonFieldType.BOOLEAN)
						.description("페이지 정렬 방식 내림차순인 경우"),
					fieldWithPath("pageableCustom.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬할 게시물이 없는 경우")
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
					fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시물의 내용"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("게시물의 공개 여부"),
					fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("게시물을 작성한 사용자의 아이디"),
					fieldWithPath("user.email").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 이메일"),
					fieldWithPath("user.nickName").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 닉네임"),
					fieldWithPath("user.introduce").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 한 줄 소개"),
					fieldWithPath("user.prologName").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 블로그명"),
					fieldWithPath("user.profileImgUrl").type(JsonFieldType.STRING)
						.description("게시물을 작성한 사용자의 프로필 이미지 주소"),
					fieldWithPath("tags").type(JsonFieldType.ARRAY).description("게시물의 태그"),
					fieldWithPath("series").type(JsonFieldType.OBJECT).description("시리즈"),
					fieldWithPath("series.title").type(JsonFieldType.STRING).description("시리즈의 이름"),
					fieldWithPath("series.posts").type(JsonFieldType.ARRAY).description("시리즈에 포함된 게시물"),
					fieldWithPath("series.posts.[].id").type(JsonFieldType.NUMBER)
						.description("시리즈에 포함된 게시물의 아이디"),
					fieldWithPath("series.posts.[].title").type(JsonFieldType.STRING)
						.description("시리즈에 포함된 게시물의 제목"),
					fieldWithPath("series.count").type(JsonFieldType.NUMBER).description("시리즈에 포함된 게시물의 개수"),
					fieldWithPath("comment").type(JsonFieldType.ARRAY).description("게시물의 댓글"),
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("게시물의 댓글 수"),
					fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("게시물의 좋아요 수")
				)));
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 수정할 수 있다.")
	void update() throws Exception {
		UpdatePostRequest update = new UpdatePostRequest(UPDATE_TITLE, UPDATE_CONTENT, "", false);

		mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/posts/{id}", postId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update))
			).andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시물의 내용"),
					fieldWithPath("tagText").type(JsonFieldType.STRING).description("게시물의 요청 태그"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("게시물의 공개 여부")
				),
				responseFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시물의 내용"),
					fieldWithPath("openStatus").type(JsonFieldType.BOOLEAN).description("게시물의 공개 여부"),
					fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("게시물을 작성한 사용자의 아이디"),
					fieldWithPath("user.email").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 이메일"),
					fieldWithPath("user.nickName").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 닉네임"),
					fieldWithPath("user.introduce").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 한 줄 소개"),
					fieldWithPath("user.prologName").type(JsonFieldType.STRING).description("게시물을 작성한 사용자의 블로그명"),
					fieldWithPath("user.profileImgUrl").type(JsonFieldType.STRING)
						.description("게시물을 작성한 사용자의 프로필 이미지 주소"),
					fieldWithPath("tags").type(JsonFieldType.ARRAY).description("게시물의 태그"),
					fieldWithPath("series").type(JsonFieldType.OBJECT).description("시리즈"),
					fieldWithPath("series.title").type(JsonFieldType.STRING).description("시리즈의 이름"),
					fieldWithPath("series.posts").type(JsonFieldType.ARRAY).description("시리즈에 포함된 게시물"),
					fieldWithPath("series.posts.[].id").type(JsonFieldType.NUMBER)
						.description("시리즈에 포함된 게시물의 아이디"),
					fieldWithPath("series.posts.[].title").type(JsonFieldType.STRING)
						.description("시리즈에 포함된 게시물의 제목"),
					fieldWithPath("series.count").type(JsonFieldType.NUMBER).description("시리즈에 포함된 게시물의 개수"),
					fieldWithPath("comment").type(JsonFieldType.ARRAY).description("게시물의 댓글"),
					fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("게시물의 댓글 수"),
					fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("게시물의 좋아요 수")
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
			.andDo(restDocs.document());
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
			.andExpect(status().isBadRequest())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").description("에러 상태 코드"),
					fieldWithPath("message").description("에러 메시지 응답")
				)
			));
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
			.andExpect(status().isBadRequest())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").description("에러 상태 코드"),
					fieldWithPath("message").description("에러 메시지 응답")
				)
			));

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
			.andExpect(status().isBadRequest())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").description("에러 상태 코드"),
					fieldWithPath("message").description("에러 메시지 응답")
				)
			));
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