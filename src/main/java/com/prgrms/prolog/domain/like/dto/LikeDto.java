package com.prgrms.prolog.domain.like.dto;

import javax.validation.constraints.NotNull;

import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

public class LikeDto {

	public record likeRequest(@NotNull Long userId,
							  @NotNull Long postId) {

		public Like from(User user, Post post) {
			return Like.builder()
				.user(user)
				.post(post)
				.build();
		}
	}
}
