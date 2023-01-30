package com.prgrms.prolog.domain.series.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.model.Series;

public class SeriesDto {
	public record CreateSeriesRequest(
		@NotBlank String title
	) {
		public static CreateSeriesRequest from(String title) {
			return new CreateSeriesRequest(title);
		}
	}

	public record SeriesResponse(
		String title,
		List<SimplePostResponse> posts,
		int count
	) {
		public static SeriesResponse from(Series series) {
			List<Post> posts = series.getPosts();
			return new SeriesResponse(
				series.getTitle(),
				posts.stream()
					.map(SimplePostResponse::from)
					.toList(),
				posts.size()
			);
		}

		public record SimplePostResponse(
			Long id,
			String title
		) {
			public static SimplePostResponse from(Post post) {
				return new SimplePostResponse(
					post.getId(),
					post.getTitle()
				);
			}
		}
	}

}
