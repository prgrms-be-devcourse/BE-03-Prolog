package com.prgrms.prolog.global.oauth;

import static com.prgrms.prolog.global.oauth.dto.KakaoUserInfo.*;
import static lombok.AccessLevel.*;

import java.util.Objects;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.prgrms.prolog.global.oauth.dto.OauthUserInfo;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class OAuthProvider {

	public static OauthUserInfo toUserProfile(OAuth2User oauth, String providerName) {
		if (Objects.equals(providerName, "kakao")) {
			return toUserInfo(oauth);
		}
		throw new IllegalArgumentException();
	}
}