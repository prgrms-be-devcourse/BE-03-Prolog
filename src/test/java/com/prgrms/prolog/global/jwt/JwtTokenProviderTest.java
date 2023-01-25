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

	private static final String ISSUER = "issuer";
	private static final String SECRET_KEY = "secretKey";
	private static final int EXPIRY_SECONDS = 2;

	private static final JwtTokenProvider jwtTokenProvider
		= new JwtTokenProvider(ISSUER,SECRET_KEY,EXPIRY_SECONDS);

	@Test
	@DisplayName("토큰 생성 및 추출")
	void createTokenAndVerifyToken() {
		// given
		String token = jwtTokenProvider.createAccessToken(Claims.from(USER_ID, USER_ROLE));
		// when
		Claims claims = jwtTokenProvider.getClaims(token);
		// then
		assertAll(
			() -> assertThat(claims.getUserId()).isEqualTo(USER_ID),
			() -> assertThat(claims.getRole()).isEqualTo(USER_ROLE)
		);
	}

	@Test
	@DisplayName("유효 시간이 지난 토큰을 사용하면 예외가 발생한다.")
	void validateToken_OverTime() throws InterruptedException {
		// given
		String token = jwtTokenProvider.createAccessToken(Claims.from(USER_ID, USER_ROLE));
		// when
		sleep(EXPIRY_SECONDS * 2000L);
		// then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

	@Test
	@DisplayName("유효하지 않은 토큰을 사용하면 예외가 발생한다.")
	void validateTokenByInvalid() {
		// given
		String token = "invalid";
		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

	@Test
	@DisplayName("올바르지 않은 시그니처로 검증 시 예외를 발생한다.")
	void validateTokenByWrongSign() {
		// given
		String invalidSecretKey = "S-Team";
		JwtTokenProvider wongTokenProvider
			= new JwtTokenProvider(ISSUER, invalidSecretKey, EXPIRY_SECONDS);
		//when
		String token = wongTokenProvider.createAccessToken(Claims.from(USER_ID, USER_ROLE));
		//then
		assertThatThrownBy(() -> jwtTokenProvider.getClaims(token))
			.isInstanceOf(JWTVerificationException.class);
	}

}