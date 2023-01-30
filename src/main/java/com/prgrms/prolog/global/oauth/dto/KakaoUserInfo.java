package com.prgrms.prolog.global.oauth.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Builder;

@Builder
public record KakaoUserInfo(
	String email,
	String nickName,
	String provider,
	String oauthId,
	String profileImgUrl
) implements OauthUserInfo {
	public static OauthUserInfo toUserInfo(OAuth2User oauth) {

		Map<String, Object> response = oauth.getAttributes();
		Map<String, Object> properties = oauth.getAttribute("properties");
		Map<String, Object> account = oauth.getAttribute("kakao_account");

		return KakaoUserInfo.builder()
			.email(String.valueOf(account.get("email")))
			.nickName(String.valueOf(properties.get("nickname")))
			.oauthId(String.valueOf(response.get("id")))
			.provider("kakao")
			.profileImgUrl(String.valueOf(properties.get("profile_image")))
			.build();
	}
}
