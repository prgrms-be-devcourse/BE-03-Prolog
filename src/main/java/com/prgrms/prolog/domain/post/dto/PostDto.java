package com.prgrms.prolog.domain.post.dto;

import static com.prgrms.prolog.domain.posttag.dto.PostTagDto.*;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.dto.SeriesDto.SeriesResponse;
import com.prgrms.prolog.domain.user.dto.UserDto.UserResponse;
import com.prgrms.prolog.domain.user.model.User;

public class PostDto {
	public record CreatePostRequest(@NotBlank @Size(max = 200) String title,
	                                @NotBlank String content,
	                                @Nullable String tagText,
	                                boolean openStatus,
	                                @Nullable String seriesTitle) {
		public static Post from(CreatePostRequest create, User user) {
			return Post.builder()
				.title(create.title)
				.content(create.content)
				.openStatus(create.openStatus)
				.user(user)
				.build();
		}
	}

	public record UpdatePostRequest(@NotBlank @Size(max = 200) String title,
	                                @NotBlank String content,
	                                @Nullable String tagText,
	                                boolean openStatus) {
	}

	public record SinglePostResponse(String title,
	                                 String content,
	                                 boolean openStatus,
	                                 UserResponse user,
	                                 Set<String> tags,
	                                 SeriesResponse series,
	                                 List<Comment> comment,
	                                 int commentCount,
	                                 int likeCount) {

		public static SinglePostResponse from(Post post) {
			return new SinglePostResponse(post.getTitle(),
				post.getContent(),
				post.isOpenStatus(),
				UserResponse.from(post.getUser()),
				PostTagsResponse.from(post.getPostTags()).tagNames(),
				SeriesResponse.from(post.getSeries()),
				post.getComments(),
				post.getComments().size(),
				post.getLikeCount());
		}
	}
}
