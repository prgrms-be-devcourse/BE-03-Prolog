package com.prgrms.prolog.domain.post.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

public class PostRequest {
	public record CreateRequest(@NotBlank @Size(max = 200) String title,
								@NotBlank String content,
								@Nullable String tagText,
								boolean openStatus) {
		public static Post toEntity(CreateRequest create, User user) {
			return Post.builder()
				.title(create.title)
				.content(create.content)
				.openStatus(create.openStatus)
				.user(user)
				.build();
		}
	}

	public record UpdateRequest(@NotBlank @Size(max = 200) String title,
								@NotBlank String content,
								@Nullable String tagText,
								boolean openStatus) {
	}
}
