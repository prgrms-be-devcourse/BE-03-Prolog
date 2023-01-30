package com.prgrms.prolog.domain.comment.dto;

import javax.validation.constraints.NotBlank;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

public class CommentDto {

	public record CreateCommentRequest(
		@NotBlank String content
	) {
		public static Comment toEntity(
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
	}

}
