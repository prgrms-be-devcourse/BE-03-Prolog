package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

public class UserResponse {

	public record findResponse(Long id,
							   String email,
							   String nickName,
							   String introduce,
							   String prologName) {

		public static findResponse toUserResponse(User user) {
			return new UserResponse.findResponse(user.getId(), user.getEmail(), user.getNickName(), user.getIntroduce(),
				user.getPrologName());
		}
	}
}
