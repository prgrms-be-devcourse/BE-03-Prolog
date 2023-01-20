package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.mockito.BDDMockito.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.domain.user.service.UserServiceImpl;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@SpringBootTest
class UserControllerTest {

	private static final JwtTokenProvider jwtTokenProvider = JWT_TOKEN_PROVIDER;
	protected MockMvc mockMvc;
	@Autowired
	RestDocumentationResultHandler restDocs;
	@MockBean
	private UserServiceImpl userService;

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
		UserInfo userInfo = getUserInfo();
		Claims claims = Claims.from(userInfo.email(), USER_ROLE);
		given(userService.findByEmail(userInfo.email())).willReturn(userInfo);
		// when
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/me")
					.header("token", jwtTokenProvider.createAccessToken(claims))
				// .header(HttpHeaders.AUTHORIZATION, "token" + jwtTokenProvider.createAccessToken(claims))
			)
			// then
			.andExpectAll(
				handler().methodName("myPage"),
				status().isOk(),
				jsonPath("email").value(userInfo.email()),
				jsonPath("nickName").value(userInfo.nickName()),
				jsonPath("introduce").value(userInfo.introduce()),
				jsonPath("prologName").value(userInfo.prologName())
			)
			// docs
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("email").type(STRING).description("이메일"),
					fieldWithPath("nickName").type(STRING).description("닉네임"),
					fieldWithPath("introduce").type(STRING).description("한줄 소개"),
					fieldWithPath("prologName").type(STRING).description("블로그 제목")
				))
			);
	}
}