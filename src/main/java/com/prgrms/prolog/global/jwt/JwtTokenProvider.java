package com.prgrms.prolog.global.jwt;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
public final class JwtTokenProvider {

	private final String issuer; // 발행자
	private final int expirySeconds; // 유효시간
	private final Algorithm algorithm; // 사용할 알고리즘
	private final JWTVerifier jwtVerifier; // jwt 변환, 검증 객체

	public JwtTokenProvider(
		@Value("${jwt.issuer}") String issuer,
		@Value("${jwt.secret-key}") String secretKey,
		@Value("${jwt.expiry-seconds}") int expirySeconds
	) {
		this.issuer = issuer;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(encodeKeyToBase64(secretKey));
		this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
	}

	private static String encodeKeyToBase64(String key) {
		return Base64.getEncoder().encodeToString(key.getBytes());
	}

	public String createAccessToken(Claims claims) {
		Date now = new Date();
		return JWT.create()
			.withIssuer(issuer)
			.withIssuedAt(now)
			.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L))
			.withClaim("email", claims.getEmail())
			.withClaim("role", claims.getRole())
			.sign(algorithm);
	}

	public Claims getClaims(String token) throws JWTVerificationException { // 기간 만료 이거나 올바르지 않은 토큰인 경우 예외 발생
		return new Claims(jwtVerifier.verify(token));
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Claims {

		private String email;
		private String role;
		private Date iat;
		private Date exp;

		protected Claims(DecodedJWT decodedJwt) {
			Claim email = decodedJwt.getClaim("email");
			if (!email.isNull()) {
				this.email = email.asString();
			}
			Claim role = decodedJwt.getClaim("role");
			if (!role.isNull()) {
				this.role = role.asString();
			}
			this.iat = decodedJwt.getIssuedAt();
			this.exp = decodedJwt.getExpiresAt();
		}

		public static Claims from(String email, String role) {
			Claims claims = new Claims();
			claims.email = email;
			claims.role = role;
			return claims;
		}

		@Override
		public String toString() {
			return "Claims{"
				+ "email='" + email + '\''
				+ ", role='" + role + '\''
				+ ", iat=" + iat
				+ ", exp=" + exp
				+ '}';
		}
	}
}
