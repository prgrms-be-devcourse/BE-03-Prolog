package com.prgrms.prolog.domain.posttag.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.prgrms.prolog.domain.posttag.model.PostTag;

public class PostTagDto {

	public record PostTagsResponse(Set<String> tagNames) {
		public static PostTagsResponse from(Set<PostTag> postTags) {
			Set<String> postTagNames = postTags.stream()
				.map(postTag -> postTag.getRootTag().getName())
				.collect(Collectors.toSet());
			return new PostTagsResponse(postTagNames);
		}
	}
}
