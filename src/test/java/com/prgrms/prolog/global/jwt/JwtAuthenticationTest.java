package com.prgrms.prolog.global.jwt;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class JwtAuthenticationTest {

	private static final String token = "validToken";

	@Test
	@DisplayName("생성")
	void token() {
		// given
		JwtAuthentication jwtAuthentication
			= new JwtAuthentication(token, USER_EMAIL);
		// when & then
		assertThat(jwtAuthentication)
			.hasFieldOrPropertyWithValue("token", token)
			.hasFieldOrPropertyWithValue("userEmail", USER_EMAIL);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("token은 null, 빈 값일 수 없다.")
	void validateTokenTest(String inputToken) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(inputToken, USER_EMAIL))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("토큰");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("email은 null, 빈 값일 수 없다.")
	void validateUserEmailTest(String inputUserEmail) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(token, inputUserEmail))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이메일");
	}
}