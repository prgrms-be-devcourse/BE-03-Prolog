package com.prgrms.prolog.domain.post.dto;

import java.util.List;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.dto.UserResponse;

public record PostResponse(String title,
						   String content,
						   boolean openStatus,
						   UserResponse.findResponse user,
						   List<Comment> comment,
						   int commentCount) {

	public static PostResponse toPostResponse(Post post) {
		return new PostResponse(post.getTitle(), post.getContent(), post.isOpenStatus(),
			UserResponse.findResponse.toUserResponse(post.getUser()), post.getComments(), post.getComments().size());
	}
}
