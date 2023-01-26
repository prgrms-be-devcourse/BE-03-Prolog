package com.prgrms.prolog.domain.series.dto;

import javax.validation.constraints.NotBlank;

public record CreateSeriesRequest(
	@NotBlank String title
) {
	public static CreateSeriesRequest of(String title) {
		return new CreateSeriesRequest(title);
	}
}
