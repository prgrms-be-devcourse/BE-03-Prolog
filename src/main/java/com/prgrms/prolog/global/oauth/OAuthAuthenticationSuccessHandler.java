package com.prgrms.prolog.global.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.prolog.domain.user.dto.UserDto.UserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final String CONTENT_TYPE = "application/json";
	private static final String SLASH = "/";

	private final OAuthService oauthService;
	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		String providerName = extractProviderName(request);
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User oauth2User) {
			UserInfo userInfo = OAuthProvider.toUserProfile(oauth2User, providerName);
			String accessToken = oauthService.login(userInfo);
			setResponse(response, accessToken); // TODO: 헤더에 넣기
		}
	}

	private void setResponse(HttpServletResponse response, String accessToken) throws IOException {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		log.debug(accessToken);
		response.getWriter().write(objectMapper.writeValueAsString(accessToken));
	}

	private String extractProviderName(HttpServletRequest request) {
		String[] splitURI = request.getRequestURI().split(SLASH);
		return splitURI[splitURI.length - 1];
	}

}
