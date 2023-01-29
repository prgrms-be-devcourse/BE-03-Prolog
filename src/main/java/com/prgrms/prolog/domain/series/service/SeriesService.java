package com.prgrms.prolog.domain.series.service;

import javax.validation.Valid;

import com.prgrms.prolog.domain.series.dto.CreateSeriesRequest;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.global.common.IdResponse;

public interface SeriesService {

	IdResponse createSeries(@Valid CreateSeriesRequest request, Long userId);

	IdResponse changeSeries(@Valid UpdateSeriesRequest request, Long postId, Long userId);

	SeriesResponse findSeriesByTitle(Long userId, String title);

	void deleteSeries(Long userId, String title);

}
