package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

public class UserDto {

	public record UserInfo(
		String email,
		String nickName,
		String introduce,
		String prologName
	) {
		public UserInfo(User user) {
			this(
				user.getEmail(),
				user.getNickName(),
				user.getIntroduce(),
				user.getPrologName()
			);
		}
	}

	public record UserProfile(
		String email,
		String nickName,
		String provider,
		String oauthId
	) {
		UserProfile(User user) {
			this(
				user.getEmail(),
				user.getNickName(),
				user.getProvider(),
				user.getOauthId()
			);
		}
	}

}
