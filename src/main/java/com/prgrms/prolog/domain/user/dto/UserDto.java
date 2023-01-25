package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

import lombok.Builder;

public class UserDto {

	@Builder
	public record UserProfile(
		Long id,
		String email,
		String nickName,
		String introduce,
		String prologName,
		String profileImgUrl
	) {
		public static UserProfile toUserProfile(User user) {
			return new UserProfile(
				user.getId(),
				user.getEmail(),
				user.getNickName(),
				user.getIntroduce(),
				user.getPrologName(),
				user.getProfileImgUrl()
			);
		}
	}

	@Builder
	public record UserInfo(
		String email,
		String nickName,
		String provider,
		String oauthId,
		String profileImgUrl
	) {

	}

	public record IdResponse(Long id) {
	}
}
