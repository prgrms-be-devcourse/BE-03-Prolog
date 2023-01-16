package com.prgrms.prolog.global.jwt;

import java.util.Objects;

public record JwtAuthentication(String token, String userEmail) {

	public JwtAuthentication {
		validateToken(token);
		validateUserEmail(userEmail);
	}

	private void validateToken(String token) {
		if (Objects.isNull(token) || token.isBlank()) {
			throw new IllegalArgumentException("토큰이 없습니다.");
		}
	}

	private void validateUserEmail(String userEmail) {
		if (Objects.isNull(userEmail) || userEmail.isBlank()) {
			throw new IllegalArgumentException("유저 이메일이 없습니다.");
		}
	}

	@Override
	public String toString() {
		return "JwtAuthentication{"
			+ "token='" + token + '\''
			+ ", userEmail='" + userEmail + '\''
			+ '}';
	}
}
