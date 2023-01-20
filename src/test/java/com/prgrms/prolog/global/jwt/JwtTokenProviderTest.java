package com.prgrms.prolog.global.jwt;

import static com.prgrms.prolog.utils.TestUtils.*;
import static java.lang.Thread.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.prgrms.prolog.global.jwt.JwtTokenProvider.Claims;

class JwtTokenProviderTest {

	private static final JwtTokenProvider jwtTokenProvider = JWT_TOKEN_PROVIDER;

	@Test
	@DisplayName("토큰 생성 및 추출")
	void createTokenAndVerifyToken() {
		// given
		String token = jwtTokenProvider.createAccessToken(Claims.from(USER_EMAIL, USER_ROLE)); // TODO: 추후에 리팩터링 고려
		// when
		Claims claims = jwtTokenProvider.getClaims(token);
		// then
		assertAll(
			() -> assertThat(claims.getEmail()).isEqualTo(USER_EMAIL),
			() -> assertThat(claims.getRole()).isEqualTo(USER_ROLE)
		);
	}

	@Test
	@DisplayName("유효 시간이 지난 토큰을 사용하면 예외가 발생한다.")
	void validateToken_OverTime() throws InterruptedException {
		// given
		String token = jwtTokenProvider.createAccessToken(Claims.from(USER_EMAIL, USER_ROLE));
		// when
		sleep(EXPIRY_SECONDS * 2000);
		// then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

	@Test
	@DisplayName("유효하지 않은 토큰을 사용하면 예외가 발생한다.")
	void validateToken_Invalid() {
		// given
		String token = "Invalid";
		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

	@Test
	@DisplayName("올바르지 않은 시그니처로 검증 시 예외를 발생한다.")
	void validateToken_WrongSign() {
		// given
		JwtTokenProvider wongTokenProvider = new JwtTokenProvider(
			ISSUER,
			"S-Team",
			0
		);
		//when
		String token = wongTokenProvider.createAccessToken(Claims.from(USER_EMAIL, USER_ROLE));
		//then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

}