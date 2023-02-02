// package com.prgrms.prolog.domain.series.api;
//
// import static com.prgrms.prolog.utils.TestUtils.*;
// import static org.springframework.http.HttpHeaders.*;
// import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
// import static org.springframework.restdocs.payload.JsonFieldType.*;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
//
// import com.prgrms.prolog.base.ControllerTest;
// import com.prgrms.prolog.domain.post.model.Post;
// import com.prgrms.prolog.domain.post.repository.PostRepository;
// import com.prgrms.prolog.domain.series.model.Series;
// import com.prgrms.prolog.domain.series.repository.SeriesRepository;
//
// class SeriesControllerTest extends ControllerTest {
//
// 	@Autowired
// 	private PostRepository postRepository;
// 	@Autowired
// 	private SeriesRepository seriesRepository;
//
// 	private Post savedPost;
// 	private Series savedSeries;
//
// 	@BeforeEach
// 	void setUpSeries() {
// 		Post post = Post.builder()
// 			.title(POST_TITLE)
// 			.content(POST_CONTENT)
// 			.openStatus(true)
// 			.user(savedUser)
// 			.build();
// 		savedPost = postRepository.save(post);
// 		Series series = Series.builder()
// 			.title(SERIES_TITLE)
// 			.user(savedUser)
// 			.post(savedPost)
// 			.build();
// 		savedSeries = seriesRepository.save(series);
// 	}
//
// 	@Test
// 	@DisplayName("자신이 가진 시리즈 중에서 제목으로 게시글 정보를 조회할 수 있다.")
// 	void findSeriesByTitleTest() throws Exception {
// 		// when
// 		mockMvc.perform(get("/api/v1/series")
// 				.header(AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
// 				.param("title", SERIES_TITLE)
// 			)
// 			// then
// 			.andExpectAll(
// 				handler().methodName("getSeriesByTitle"),
// 				status().isOk())
// 			// docs
// 			.andDo(restDocs.document(
// 				responseFields(
// 					fieldWithPath("title").type(STRING).description("시리즈 제목"),
// 					fieldWithPath("posts").type(ARRAY).description("게시글 목록"),
// 					fieldWithPath("posts.[].id").type(NUMBER).description("게시글 아이디"),
// 					fieldWithPath("posts.[].title").type(STRING).description("게시글 제목"),
// 					fieldWithPath("count").type(NUMBER).description("게시물 개수")
// 				))
// 			);
//
// 	}
// }
