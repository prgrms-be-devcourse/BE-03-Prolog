package com.prgrms.prolog.global.oauth;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.user.service.UserService;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;
import com.prgrms.prolog.global.oauth.dto.OauthUserInfo;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OAuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	@Transactional
	public String login(OauthUserInfo userInfo) {
		Long userId = userService.signUp(userInfo);
		return jwtTokenProvider.createAccessToken(
			Claims.from(userId,"ROLE_USER"));
	}
}
