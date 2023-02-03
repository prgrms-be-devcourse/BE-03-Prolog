package com.prgrms.prolog.global.jwt;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.prgrms.prolog.global.config.MessageKeyConfig;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private String credentials;

	public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		this(principal, null, authorities);
	}

	public JwtAuthenticationToken(String principal, String credentials) {
		super(null);
		super.setAuthenticated(false);

		this.principal = principal;
		this.credentials = credentials;
	}

	public JwtAuthenticationToken(
		Object principal, String credentials, Collection<? extends GrantedAuthority> authorities
	) {
		super(authorities);
		super.setAuthenticated(true);

		this.principal = principal;
		this.credentials = credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public String getCredentials() {
		return credentials;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) {
		if (isAuthenticated) {
			throw new IllegalArgumentException(messageKey().exception().jwtAuthentication().isAuthenticated().endKey());
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

	@Override
	public String toString() {
		return "JwtAuthenticationToken{"
			+ "principal=" + principal
			+ ", credentials='" + credentials + '\''
			+ '}';
	}
}
