package com.prgrms.prolog.domain.series.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.series.dto.CreateSeriesRequest;
import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.common.IdResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeriesServiceImpl implements SeriesService {

	private final SeriesRepository seriesRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public IdResponse createSeries(@Valid CreateSeriesRequest request, Long userId) {
		User findUser = getFindUserBy(userId);
		Series series = buildSeries(request.title(), findUser);
		return new IdResponse(seriesRepository.save(series).getId());
	}

	@Override
	public SeriesResponse findSeriesByTitle(Long userId, String title) {
		return seriesRepository.findByIdAndTitle(userId, title)
			.map(SeriesResponse::toSeriesResponse)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	private Series buildSeries(String title, User user) {
		return Series.builder()
			.title(title)
			.user(user)
			.build();
	}

	private User getFindUserBy(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}
}
