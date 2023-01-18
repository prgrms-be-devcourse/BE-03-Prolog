package com.prgrms.prolog.global.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.prgrms.prolog.domain.user.dto.UserDto.UserProfile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final String CONTENT_TYPE = "application/json";
	private static final String SLASH = "/";

	private final OAuthService oauthService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		String providerName = extractProviderName(request);
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User oauth2User) {
			UserProfile userProfile = OAuthProvider.toUserProfile(oauth2User, providerName);
			String accessToken = oauthService.login(userProfile);
			setResponse(response, accessToken);
		}
	}

	private void setResponse(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setHeader("token", accessToken);
	}

	private String extractProviderName(HttpServletRequest request) {
		String[] splitURI = request.getRequestURI().split(SLASH);
		return splitURI[splitURI.length - 1];
	}

}
