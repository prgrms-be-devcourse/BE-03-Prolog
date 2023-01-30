package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SeriesServiceImpl implements SeriesService {

	private final UserRepository userRepository;
	private final SeriesRepository seriesRepository;

	@Override
	@Transactional
	public Long createSeries(@Valid CreateSeriesRequest request, Long userId) {
		User findUser = getFindUserBy(userId);
		Series series = buildSeries(request.title(), findUser);
		return seriesRepository.save(series).getId();
	}

	@Override
	public SeriesResponse getSeries(String title, Long userId) {
		return seriesRepository.findByIdAndTitle(userId, title)
			.map(SeriesResponse::from)
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
