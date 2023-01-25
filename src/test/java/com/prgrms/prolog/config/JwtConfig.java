package com.prgrms.prolog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.prgrms.prolog.global.jwt.JwtTokenProvider;

@TestConfiguration
public class JwtConfig {

	@Bean
	public JwtTokenProvider jwtTokenProvider(
		@Value("${jwt.issuer}") String issuer,
		@Value("${jwt.secret-key}") String secretKey,
		@Value("${jwt.expiry-seconds}") int expirySeconds
	) {
		return new JwtTokenProvider(issuer,secretKey,expirySeconds);
	}
}
