package com.prgrms.prolog.global.jwt;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class JwtAuthenticationTest {

	private static final String token = "validToken";

	@Test
	@DisplayName("생성")
	void token() {
		// given
		JwtAuthentication jwtAuthentication
			= new JwtAuthentication(token, USER_ID);
		// when & then
		assertThat(jwtAuthentication)
			.hasFieldOrPropertyWithValue("token", token)
			.hasFieldOrPropertyWithValue("id", USER_ID);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("token은 null, 빈 값일 수 없다.")
	void validateTokenTest(String inputToken) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(inputToken, USER_ID))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(longs = {0L, -1L, -100L})
	@DisplayName("userId는 null, 0 이하일 수 없다.")
	void validateUserEmailTest(Long inputUserId) {
		//given & when & then
		assertThatThrownBy(() -> new JwtAuthentication(token, inputUserId))
			.isInstanceOf(IllegalArgumentException.class);
	}
}