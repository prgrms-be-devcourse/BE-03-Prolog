package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

import lombok.Builder;

public class UserDto {

	@Builder
	public record UserResponse(
		Long id,
		String email,
		String nickName,
		String introduce,
		String prologName,
		String profileImgUrl
	) {
		public static UserResponse from(User user) {
			return new UserResponse(
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
	public record UpdateUserRequest(String email,
									String nickName,
									String introduce,
									String prologName
	) {
	}

}
