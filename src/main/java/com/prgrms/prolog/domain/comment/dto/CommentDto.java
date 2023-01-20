package com.prgrms.prolog.domain.comment.dto;

import javax.validation.constraints.NotBlank;

public class CommentDto {

	public record CreateCommentRequest(
		@NotBlank String content
	) {
	}

	public record UpdateCommentRequest(
		@NotBlank String content
	) {
	}

}
