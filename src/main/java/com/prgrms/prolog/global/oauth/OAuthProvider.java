package com.prgrms.prolog.global.oauth;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthProvider {

	public static UserProfile toUserProfile(OAuth2User oauth, String providerName) {
		Map<String, Object> response = oauth.getAttributes();
		Map<String, Object> properties = oauth.getAttribute("properties");
		Map<String, Object> account = oauth.getAttribute("kakao_account");

		return UserProfile.builder()
			.email(String.valueOf(account.get("email")))
			.nickName(String.valueOf(properties.get("nickname")))
			.oauthId(String.valueOf(response.get("id")))
			.provider(providerName)
			.build();
	}
}
