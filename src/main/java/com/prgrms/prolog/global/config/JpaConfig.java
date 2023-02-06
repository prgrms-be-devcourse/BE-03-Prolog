package com.prgrms.prolog.global.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prgrms.prolog.global.jwt.JwtAuthentication;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
@Configuration
public class JpaConfig {

	@Bean
	public AuditorAware<String> auditorAwareProvider() {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.filter(Authentication::isAuthenticated)
			.map(authentication -> (JwtAuthentication)authentication.getPrincipal())
			.map(user -> user.id().toString());
	}
}
