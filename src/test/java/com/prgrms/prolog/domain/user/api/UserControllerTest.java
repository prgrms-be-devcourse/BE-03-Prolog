package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.prgrms.prolog.base.ControllerTest;

class UserControllerTest extends ControllerTest {

	@Test
	@DisplayName("사용자는 자신의 프로필 정보를 확인할 수 있다")
	void userPage() throws Exception {
		// when
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
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