package com.prgrms.prolog.domain.user.dto;

import com.prgrms.prolog.domain.user.model.User;

import lombok.Builder;

public class UserDto {

	public record UserProfile(
		Long id,
		String email,
		String nickName,
		String introduce,
		String prologName,
		String profileImgUrl
	) {
		@Builder
		public UserProfile(Long id, String email, String nickName, String introduce, String prologName,
			String profileImgUrl) {
			this.id = id;
			this.email = email;
			this.nickName = nickName;
			this.introduce = introduce;
			this.prologName = prologName;
			this.profileImgUrl = profileImgUrl;
		}

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

	// TODO : OauthDto Class로 분리
	public record UserInfo(
		String email,
		String nickName,
		String provider,
		String oauthId,
		String profileImgUrl
	) {
		@Builder
		public UserInfo(String email, String nickName, String provider, String oauthId, String profileImgUrl) {
			this.email = email;
			this.nickName = nickName;
			this.provider = provider;
			this.oauthId = oauthId;
			this.profileImgUrl = profileImgUrl;
		}
	}

	public record UpdateUserRequest(String email,
									String nickName,
									String introduce,
									String prologName
	) {
	}

}
