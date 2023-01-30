package com.prgrms.prolog.domain.post.dto;

import com.prgrms.prolog.domain.post.model.Post;

// TODO : SimplePostResponse
public record PostInfo(
	Long id,
	String title
) {
	public static PostInfo toPostInfo(Post post) {
		return new PostInfo(
			post.getId(),
			post.getTitle()
		);
	}
}