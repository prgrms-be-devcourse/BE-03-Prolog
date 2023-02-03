package com.prgrms.prolog.global.jwt;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;

import java.util.Objects;

public record JwtAuthentication(String token, Long id) {

	public JwtAuthentication {
		validateToken(token);
		validateId(id);
	}


	private void validateToken(String token) {
		if (Objects.isNull(token) || token.isBlank()) {
			throw new IllegalArgumentException(messageKey().exception().jwtAuthentication().token().notExists().endKey());
		}
	}

	private void validateId(Long userId) {
		if (Objects.isNull(userId) || userId <= 0L) {
			throw new IllegalArgumentException(messageKey().exception().jwtAuthentication().user().id().endKey());
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
