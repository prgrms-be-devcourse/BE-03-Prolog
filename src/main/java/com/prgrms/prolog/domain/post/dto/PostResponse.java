package com.prgrms.prolog.domain.post.dto;

import static com.prgrms.prolog.domain.posttag.dto.PostTagDto.*;
import static com.prgrms.prolog.domain.user.dto.UserDto.UserProfile.*;

import java.util.List;
import java.util.Set;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.user.dto.UserDto.UserProfile;

// TODO : PostResponse 를 분리한다. (MainPostResponse, SinglePostResponse, ...)
public record PostResponse(String title,
						   String content,
						   boolean openStatus,
						   UserProfile user,
						   Set<String> tags,
						   SeriesResponse seriesResponse,
						   List<Comment> comment,
						   int commentCount,
						   int likeCount) {

	// TODO : Post -> from(...) 으로 통일
	// TODO : PostResponse -> from(...) 으로 통일
	public static PostResponse from(Post post) {
		return new PostResponse(post.getTitle(),
			post.getContent(),
			post.isOpenStatus(),
			toUserProfile(post.getUser()),
			PostTagsResponse.from(post.getPostTags()).tagNames(),
			SeriesResponse.toSeriesResponse(post.getSeries()),
			post.getComments(),
			post.getComments().size(),
			post.getLikeCount());
	}
}
