package com.prgrms.prolog.global.jwt;

import static org.springframework.http.HttpHeaders.*;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_TYPE = "Bearer";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = getToken(request);
		if (token != null) {
			JwtAuthenticationToken authentication = createAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().endsWith("tokens")
			&& request.getMethod().equalsIgnoreCase("POST");
	}

	private String getToken(HttpServletRequest request) {
		String token = extractToken(request);
		if (token != null) {
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}
		return null;
	}

	private String extractToken(HttpServletRequest request) {
		String headerValue = request.getHeader(AUTHORIZATION);
		if (headerValue != null) {
			return headerValue.split(BEARER_TYPE)[1].trim();
		}
		return null;
	}

	private JwtAuthenticationToken createAuthentication(String token) {
		Claims claims = jwtTokenProvider.getClaims(token);
		JwtAuthentication principal
			= new JwtAuthentication(token, claims.getUserId());

		return new JwtAuthenticationToken(principal, getAuthorities(claims.getRole()));
	}

	private List<GrantedAuthority> getAuthorities(String role) {
		return List.of(new SimpleGrantedAuthority(role));
	}

}
