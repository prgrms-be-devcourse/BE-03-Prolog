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
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.prolog.domain.user.dto.UserDto.UserInfo;
import com.prgrms.prolog.domain.user.dto.UserDto.UserProfile;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService; // 빈으로 등록해서 주입 받고 싶으면 어떻게 해야하나요? 구현체말고 인터페이스를 주입 받고 싶습니다!

	@Nested
	@DisplayName("사용자 조회 #10")
	class SignUpAndLogin {

		@Test
		@DisplayName("이메일로 사용자 정보를 조회할 수 있다")
		void findByEmailTest() {
			// given
			User user = getUser();
			given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(user));
			// when
			UserInfo foundUser = userService.findByEmail(USER_EMAIL);
			// then
			then(userRepository).should().findByEmail(USER_EMAIL);
			assertThat(foundUser)
				.hasFieldOrPropertyWithValue("email", user.getEmail())
				.hasFieldOrPropertyWithValue("nickName", user.getNickName())
				.hasFieldOrPropertyWithValue("introduce", user.getIntroduce())
				.hasFieldOrPropertyWithValue("prologName", user.getPrologName());
		}

		@DisplayName("이메일 정보에 일치하는 사용자가 없으면 NotFoundException")
		@Test
		void notFoundMatchUser() {
			given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
			String unsavedEmail = "unsaved@test.com";
			assertThatThrownBy(() -> userService.findByEmail(unsavedEmail))
				.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	@DisplayName("회원가입 및 로그인 #9")
	class FindUserInfo {
		@Test
		@DisplayName("등록된 사용자라면 로그인할 수 있다.")
		void loginTest() {
			// given
			User user = getUser();
			UserProfile savedUserProfile = getUserProfile();
			given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
			// when
			UserInfo foundUserInfo = userService.login(savedUserProfile);
			// then
			then(userRepository).should().findByEmail(savedUserProfile.email());
			assertThat(foundUserInfo)
				.hasFieldOrPropertyWithValue("email", user.getEmail())
				.hasFieldOrPropertyWithValue("email", savedUserProfile.email())
				.hasFieldOrPropertyWithValue("nickName", user.getNickName())
				.hasFieldOrPropertyWithValue("nickName", savedUserProfile.nickName())
				.hasFieldOrPropertyWithValue("introduce", user.getIntroduce())
				.hasFieldOrPropertyWithValue("prologName", user.getPrologName());
		}

		@Test
		@DisplayName("등록되지 않은 사용자가 로그인하는 경우 자동으로 회원가입이 진행된다.")
		void defaultSignUpTest() {
			// given
			User user = getUser();
			UserProfile unsavedUserProfile = getUserProfile();
			given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
			given(userRepository.save(any(User.class))).willReturn(user);
			// when
			UserInfo foundUserInfo = userService.login(unsavedUserProfile);
			// then
			then(userRepository).should().findByEmail(unsavedUserProfile.email());
			then(userRepository).should().save(any(User.class));
			assertThat(foundUserInfo)
				.hasFieldOrPropertyWithValue("email", user.getEmail())
				.hasFieldOrPropertyWithValue("nickName", user.getNickName())
				.hasFieldOrPropertyWithValue("introduce", user.getIntroduce())
				.hasFieldOrPropertyWithValue("prologName", user.getPrologName());
		}
	}
}
