package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.prgrms.prolog.base.ControllerTest;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

class UserControllerTest extends ControllerTest {

	private User savedUser2;
	private String ACCESS_TOKEN2;

	@BeforeEach
	void setUser() {
		User user = User.builder()
			.email("user2@email")
			.nickName("닉네임입니당")
			.prologName("user2의 prolog")
			.provider("kakao")
			.oauthId("oauthId")
			.introduce("자기소개")
			.profileImgUrl("imageUrl")
			.build();

		savedUser2 = userRepository.save(user);
		JwtTokenProvider.Claims claims = JwtTokenProvider.Claims.from(savedUser2.getId(), "ROLE_USER");
		ACCESS_TOKEN2 = jwtTokenProvider.createAccessToken(claims);
	}

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

	@Test
	@DisplayName("유저 아이디로 유저 정보를 조회할 수 있다.")
	void getUserProfile() throws Exception {
		// when
		mockMvc.perform(RestDocumentationRequestBuilders
				.get("/api/v1/user/{userId}", savedUserId)
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
			)
			// then
			.andExpectAll(
				handler().methodName("getUserProfile"),
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

	@Test
	@DisplayName("자신의 프로필을 수정할 수 있다.")
	void updateMyProfile() throws Exception {
		UpdateUserRequest request = UpdateUserRequest.builder()
			.email("updateUserEamil@email.com")
			.nickName("수정된 닉네임")
			.prologName("수정된 블로그명")
			.introduce("수정된 자기소개")
			.build();

		// when
		mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			// then
			.andExpectAll(
				handler().methodName("updateMyProfile"),
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

	@Test
	@DisplayName("자신의 계정을 삭제할 수 있다")
	void deleteMyProfile() throws Exception {
		// when
		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/user/me")
				.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + ACCESS_TOKEN2)
			)
			// then
			.andExpectAll(
				handler().methodName("deleteMyProfile"),
				status().isNoContent()
			);
	}
}