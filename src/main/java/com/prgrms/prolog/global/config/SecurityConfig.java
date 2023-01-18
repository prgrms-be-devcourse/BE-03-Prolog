package com.prgrms.prolog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prgrms.prolog.global.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.prolog.global.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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
			// 세션 설정 -> 사용 X
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			// jwt 필터 추가
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		;
	}
}