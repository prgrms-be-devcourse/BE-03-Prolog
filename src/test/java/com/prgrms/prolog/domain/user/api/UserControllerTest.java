package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
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
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.domain.user.service.UserServiceImpl;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@Transactional
class UserControllerTest {

	protected MockMvc mockMvc;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private RestDocumentationResultHandler restDocs;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(provider))
			.apply(springSecurity())
			.alwaysDo(restDocs)
			.build();
	}

	@Test
	@DisplayName("사용자는 자신의 프로필 정보를 확인할 수 있다")
	void userPage() throws Exception {
		// given
		User savedUser = userRepository.save(USER);
		Claims claims = Claims.from(savedUser.getId(), USER_ROLE);
		// when
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + jwtTokenProvider.createAccessToken(claims))
			)
			// then
			.andExpectAll(
				handler().methodName("getMyProfile"),
				status().isOk())
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("id").type(NUMBER).description("ID"),
					fieldWithPath("email").type(STRING).description("이메일"),
					fieldWithPath("nickName").type(STRING).description("닉네임"),
					fieldWithPath("introduce").type(STRING).description("한줄 소개"),
					fieldWithPath("prologName").type(STRING).description("블로그 제목"),
					fieldWithPath("profileImgUrl").type(STRING).description("프로필 이미지")
				))
			);
	}
}