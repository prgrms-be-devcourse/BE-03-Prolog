package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

import lombok.Builder;

public class UserDto {

	@Builder
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

	@Builder
	public record UserProfile(
		String email,
		String nickName,
		String provider,
		String oauthId
	) {

	}

}
