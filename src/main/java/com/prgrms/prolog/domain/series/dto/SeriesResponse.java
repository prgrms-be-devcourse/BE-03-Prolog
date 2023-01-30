package com.prgrms.prolog.domain.series.dto;

import java.util.List;

import com.prgrms.prolog.domain.post.dto.PostInfo;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.model.Series;

public record SeriesResponse(
	String title,
	// TODO : PostInfo를 SeriesResponse 내에서 해결한다.
	List<PostInfo> posts,
	int count
) {
	public static SeriesResponse toSeriesResponse(Series series) {
		List<Post> posts = series.getPosts();
		return new SeriesResponse(
			series.getTitle(),
			posts.stream()
				.map(PostInfo::toPostInfo).toList(),
			posts.size()
		);
	}
}
