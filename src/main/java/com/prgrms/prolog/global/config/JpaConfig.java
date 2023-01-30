package com.prgrms.prolog.global.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prgrms.prolog.global.jwt.JwtAuthentication;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

	private static final GrantedAuthority PERMISSION_ROLE
		= new SimpleGrantedAuthority("USER");

	@Bean
	public AuditorAware<String> auditorAwareProvider() {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.map(this::getCreatorInfo);
	}

	private String getCreatorInfo(Authentication authentication) {
		if (isValidAuthentication(authentication)) {
			JwtAuthentication user = (JwtAuthentication)authentication.getPrincipal();
			return user.id().toString();
		}
		return null;
	}

	private boolean isValidAuthentication(Authentication authentication) {
		return authentication != null && authentication.isAuthenticated()
			&& authentication.getAuthorities().contains(PERMISSION_ROLE);
	}
}
