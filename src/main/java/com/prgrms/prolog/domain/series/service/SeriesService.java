package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;

import javax.validation.Valid;

public interface SeriesService {

	Long createSeries(@Valid CreateSeriesRequest request, Long userId);

	SeriesResponse getSeries(String title, Long userId);

}
