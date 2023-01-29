package com.prgrms.prolog.domain.series.service;

import javax.validation.Valid;

import com.prgrms.prolog.domain.series.dto.CreateSeriesRequest;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.global.common.IdResponse;

public interface SeriesService {

	IdResponse createSeries(@Valid CreateSeriesRequest request, Long userId);

	SeriesResponse findSeriesByTitle(Long userId, String title);

}
