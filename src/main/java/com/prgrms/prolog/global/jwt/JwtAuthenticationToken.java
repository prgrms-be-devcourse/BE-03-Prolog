package com.prgrms.prolog.global.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

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
			throw new IllegalArgumentException("인증 정보를 확인할 수 없는 메서드 주입은 지원하지 않습니다. 생성자를 통해 생성해야 합니다.");
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
