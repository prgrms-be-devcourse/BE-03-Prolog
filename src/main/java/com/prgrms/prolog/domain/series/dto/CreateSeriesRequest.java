package com.prgrms.prolog.domain.series.dto;

import javax.validation.constraints.NotBlank;

public record CreateSeriesRequest(
	@NotBlank String title
) {
}
