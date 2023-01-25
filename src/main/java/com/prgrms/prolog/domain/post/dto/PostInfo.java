package com.prgrms.prolog.domain.post.dto;

import com.prgrms.prolog.domain.post.model.Post;

public record PostInfo(
	String title,
	String content
) {
	public static PostInfo toPostInfo(Post post) {
		return new PostInfo(
			post.getTitle(),
			post.getContent()
		);
	}}
