package com.prgrms.prolog.domain.post.dto;

import static com.prgrms.prolog.domain.posttag.dto.PostTagDto.*;
import static com.prgrms.prolog.domain.user.dto.UserDto.UserProfile.*;

import java.util.List;
import java.util.Set;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.user.dto.UserDto.UserProfile;

public record PostResponse(String title,
						   String content,
						   boolean openStatus,
						   UserProfile user,
						   Set<String> tags,
						   SeriesResponse seriesResponse,
						   List<Comment> comment,
						   int commentCount) {

	public static PostResponse toPostResponse(Post post) {
		return new PostResponse(post.getTitle(),
			post.getContent(),
			post.isOpenStatus(),
			toUserProfile(post.getUser()),
			PostTagsResponse.from(post.getPostTags()).tagNames(),
			SeriesResponse.toSeriesResponse(post.getSeries()),
			post.getComments(),
			post.getComments().size());
	}
}
