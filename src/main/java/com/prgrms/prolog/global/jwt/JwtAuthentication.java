package com.prgrms.prolog.global.jwt;

import java.util.Objects;

public record JwtAuthentication(String token, Long id) {

	public JwtAuthentication {
		validateToken(token);
		validateId(id);
	}


	private void validateToken(String token) {
		if (Objects.isNull(token) || token.isBlank()) {
			throw new IllegalArgumentException("토큰이 없습니다.");
		}
	}

	private void validateId(Long userId) {
		if (Objects.isNull(userId) || userId <= 0L) {
			throw new IllegalArgumentException("유저의 ID가 없습니다.");
		}
	}

	@Override
	public String toString() {
		return "JwtAuthentication{" +
			"token='" + token + '\'' +
			", id='" + id + '\'' +
			'}';
	}
}
