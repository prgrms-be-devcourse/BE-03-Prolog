package com.prgrms.prolog.domain.comment.dto;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import javax.validation.constraints.NotBlank;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

public class CommentDto {

	public record CreateCommentRequest(
		@NotBlank String content
	) {
		public static CreateCommentRequest from(String content) {
			return new CreateCommentRequest(content);
		}

		public static Comment from(
			CreateCommentRequest createCommentRequest,
			User user,
			Post post
		) {
			return Comment.builder()
				.content(createCommentRequest.content)
				.user(user)
				.post(post)
				.build();
		}
	}

	public record UpdateCommentRequest(
		@NotBlank String content
	) {
		public static UpdateCommentRequest from(String content) {
			return new UpdateCommentRequest(content);
		}
	}

	public record SingleCommentResponse(
		SimpleUserInfo user,
		String content
	) {
		public static SingleCommentResponse from(UserResponse user, String content) {
			return new SingleCommentResponse(SimpleUserInfo.from(user), content);
		}
	}

	public record SimpleUserInfo(
		Long userId,
		String nickName,
		String profileImgUrl
	) {
		public static SimpleUserInfo from(UserResponse user) {
			return new SimpleUserInfo(user.id(), user.nickName(), user.profileImgUrl());
		}
	}
}
