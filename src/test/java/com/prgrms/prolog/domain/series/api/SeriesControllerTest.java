package com.prgrms.prolog.domain.series.api;

import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@Transactional
class SeriesControllerTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private RestDocumentationResultHandler restDocs;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private SeriesRepository seriesRepository;

	private MockMvc mockMvc;
	private User savedUser;
	private Post savedPost;
	private Series savedSeries;

	@BeforeEach
	void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(provider))
			.apply(springSecurity())
			.alwaysDo(restDocs)
			.build();
		savedUser = userRepository.save(USER);
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

	@Test
	@DisplayName("자신이 가진 시리즈 중에서 제목으로 게시글 정보를 조회할 수 있다.")
	void findSeriesByTitleTest() throws Exception {
		// given
		Claims claims = Claims.from(savedUser.getId(), USER_ROLE);
		// when
		mockMvc.perform(get("/api/v1/series")
				.header(AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
				.param("title", SERIES_TITLE)
			)
			// then
			.andExpectAll(
				handler().methodName("findSeriesByTitle"),
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("title").type(STRING).description("시리즈 제목"),
					fieldWithPath("posts").type(ARRAY).description("게시글 목록"),
					fieldWithPath("posts.[].id").type(NUMBER).description("게시글 아이디"),
					fieldWithPath("posts.[].title").type(STRING).description("게시글 제목"),
					fieldWithPath("count").type(NUMBER).description("게시물 개수")
				))
			);

	}
}
