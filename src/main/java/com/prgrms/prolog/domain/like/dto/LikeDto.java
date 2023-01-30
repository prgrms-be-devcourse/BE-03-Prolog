package com.prgrms.prolog.domain.like.dto;

import javax.validation.constraints.NotNull;

public class LikeDto {

	public record likeRequest(@NotNull Long userId,
							  @NotNull Long postId) {
	}
}
