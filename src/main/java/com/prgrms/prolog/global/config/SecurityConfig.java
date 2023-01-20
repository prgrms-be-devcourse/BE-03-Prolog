package com.prgrms.prolog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.prgrms.prolog.global.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.prolog.global.jwt.JwtAuthenticationFilter;
import com.prgrms.prolog.global.oauth.OAuthAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final OAuthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
			.antMatchers("/docs/**").permitAll()
			.anyRequest().authenticated()
			.and()
			// REST API 기반이기 때문에 사용 X
			.httpBasic().disable()
			.formLogin().disable()
			.rememberMe().disable()
			.csrf().disable()
			.requestCache().disable()
			.headers().disable()
			.logout()
			.and()
			// 세션 설정 -> 사용 X
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			//OAuth 로그인
			.oauth2Login()
			.successHandler(oauthAuthenticationSuccessHandler)
			.and()
			// jwt 필터 추가
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.addFilterBefore(jwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
		;
	}
}
