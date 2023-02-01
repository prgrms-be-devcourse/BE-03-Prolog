package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;

import java.util.List;

import javax.validation.Valid;

public interface SeriesService {

	Long createSeries(@Valid CreateSeriesRequest request, Long userId);

	SeriesResponse getSeriesById(Long userId, Long seriesId);

	List<SeriesSimpleResponse> getAllSeries(Long userId);

	SeriesResponse updateSeries(@Valid UpdateSeriesRequest updateSeriesRequest, Long userId, Long seriesId);

	void deleteSeries(Long userId, Long seriesId);
}
