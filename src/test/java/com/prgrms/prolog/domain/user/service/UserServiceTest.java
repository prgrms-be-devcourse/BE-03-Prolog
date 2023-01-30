package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import com.prgrms.prolog.base.ServiceTest;
import com.prgrms.prolog.domain.user.dto.UserDto.UserResponse;
import com.prgrms.prolog.domain.user.model.User;

class UserServiceTest extends ServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Nested
	@DisplayName("사용자 조회 #10")
	class SignUpAndLogin {

		@Test
		@DisplayName("userId를 통해서 사용자 정보를 조회할 수 있다")
		void findByEmailTest() {
			try (MockedStatic<UserResponse> userProfile = mockStatic(UserResponse.class)) {

				//given
				given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
				given(UserResponse.from(USER)).willReturn(USER_PROFILE);

				// when
				UserResponse foundUser = userService.getUserProfile(USER_ID);

				// then
				then(userRepository).should().findById(USER_ID);
				assertThat(foundUser)
					.hasFieldOrPropertyWithValue("email", USER_EMAIL)
					.hasFieldOrPropertyWithValue("nickName", USER_NICK_NAME)
					.hasFieldOrPropertyWithValue("introduce", USER_INTRODUCE)
					.hasFieldOrPropertyWithValue("prologName", USER_PROLOG_NAME)
					.hasFieldOrPropertyWithValue("profileImgUrl", USER_PROFILE_IMG_URL);
			}
		}

		@DisplayName("userId에 해당하는 사용자가 없으면 IllegalArgumentException")
		@Test
		void notFoundMatchUser() {
			//given
			given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
			//when & then
			assertThatThrownBy(() -> userService.getUserProfile(UNSAVED_USER_ID))
				.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	@DisplayName("회원가입 #9")
	class FindUserProfile {
		@Test
		@DisplayName("등록된 사용자는 회원 가입 절차 없이 등록된 사용자 ID를 반환 받을 수 있다.")
		void signUpTest() {
			// given
			given(userRepository.findByProviderAndOauthId(PROVIDER,OAUTH_ID))
				.willReturn(Optional.of(user));
			given(user.getId()).willReturn(USER_ID);
			// when
			userService.signUp(USER_INFO);
			// then
			then(userRepository).should().findByProviderAndOauthId(PROVIDER,OAUTH_ID);
		}

		@Test
		@DisplayName("등록되지 않은 사용자는 자동으로 회원가입이 진행된다.")
		void defaultSignUpTest() {
			// given
			given(userRepository.findByProviderAndOauthId(PROVIDER, OAUTH_ID))
				.willReturn(Optional.empty());
			given(userRepository.save(any(User.class))).willReturn(user);
			given(user.getId()).willReturn(USER_ID);
			// when
			userService.signUp(USER_INFO);
			// then
			then(userRepository).should().findByProviderAndOauthId(PROVIDER, OAUTH_ID);
			then(userRepository).should().save(any(User.class));
		}
	}
}
