package com.prgrms.prolog.global.jwt;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.prgrms.prolog.global.jwt.JwtTokenProvider.Claims;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String headerKey = "token";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
		throws ServletException, IOException {

		String token = getToken(req);
		if (token != null) {
			JwtAuthenticationToken authentication = createAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(req, res);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().endsWith("tokens")
			&& request.getMethod().equalsIgnoreCase("POST");
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(headerKey);
		if (token != null) {
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}
		return null;
	}

	private JwtAuthenticationToken createAuthentication(String token) {
		Claims claims = jwtTokenProvider.getClaims(token);
		return new JwtAuthenticationToken(
			new JwtAuthentication(token, claims.getEmail()), getAuthorities(claims.getRole())
		);
	}

	private List<GrantedAuthority> getAuthorities(String role) {
		return List.of(new SimpleGrantedAuthority(role));
	}
}
