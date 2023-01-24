package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.prolog.domain.user.dto.UserDto.IdResponse;
import com.prgrms.prolog.domain.user.dto.UserDto.UserProfile;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private User userMock;

	@InjectMocks
	private UserServiceImpl userService; // 빈으로 등록해서 주입 받고 싶으면 어떻게 해야하나요? 구현체말고 인터페이스를 주입 받고 싶습니다!

	@Nested
	@DisplayName("사용자 조회 #10")
	class SignUpAndLogin {

		@Test
		@DisplayName("userId를 통해서 사용자 정보를 조회할 수 있다")
		void findByEmailTest() {
			try (MockedStatic<UserProfile> userProfile = mockStatic(UserProfile.class)) {

				//given
				given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
				given(UserProfile.toUserProfile(USER)).willReturn(USER_PROFILE);

				// when
				UserProfile foundUser = userService.findUserProfileByUserId(USER_ID);

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
			Long unsavedUserId = 100L;
			given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
			//when & then
			assertThatThrownBy(() -> userService.findUserProfileByUserId(unsavedUserId))
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
				.willReturn(Optional.of(userMock));
			given(userMock.getId()).willReturn(USER_ID);
			// when
			IdResponse userId = userService.signUp(USER_INFO);
			// then
			then(userRepository).should().findByProviderAndOauthId(PROVIDER,OAUTH_ID);
		}

		@Test
		@DisplayName("등록되지 않은 사용자는 자동으로 회원가입이 진행된다.")
		void defaultSignUpTest() {
			// given
			given(userRepository.findByProviderAndOauthId(PROVIDER, OAUTH_ID))
				.willReturn(Optional.empty());
			given(userRepository.save(any(User.class))).willReturn(userMock);
			given(userMock.getId()).willReturn(USER_ID);
			// when
			IdResponse userId = userService.signUp(USER_INFO);
			// then
			then(userRepository).should().findByProviderAndOauthId(PROVIDER, OAUTH_ID);
			then(userRepository).should().save(any(User.class));
		}
	}
}
