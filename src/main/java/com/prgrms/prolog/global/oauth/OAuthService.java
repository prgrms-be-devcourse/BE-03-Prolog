package com.prgrms.prolog.global.oauth;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.user.service.UserService;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OAuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	@Transactional
	public String login(UserProfile userProfile) {
		UserInfo user = userService.login(userProfile);
		return jwtTokenProvider.createAccessToken(Claims.from(user.email(), "ROLE_USER"));
	}
}
