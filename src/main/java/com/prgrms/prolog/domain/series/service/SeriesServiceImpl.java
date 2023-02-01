package com.prgrms.prolog.domain.series.service;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;

import java.util.List;

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

	private static final int FAIL = 0;

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
	public SeriesResponse getSeriesById(Long userId, Long seriesId) {
		return seriesRepository.joinPostFindByIdAndUserId(seriesId, userId)
			.map(SeriesResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	@Override
	public List<SeriesSimpleResponse> getAllSeries(Long userId) {
		return seriesRepository.joinPostFindByUserId(userId)
			.stream()
			.map(SeriesSimpleResponse::from)
			.toList();
	}

	@Override
	@Transactional
	public SeriesResponse updateSeries(@Valid UpdateSeriesRequest updateSeriesRequest, Long userId, Long seriesId) {
		return seriesRepository.joinPostFindByIdAndUserId(seriesId, userId)
			.map(series -> series.changeSeriesTitle(updateSeriesRequest))
			.map(SeriesResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	@Override
	@Transactional
	public void deleteSeries(Long userId, Long seriesId) {
		int result = seriesRepository.deleteByIdAndUserId(userId, seriesId);
		if (result == FAIL) {
			throw new IllegalArgumentException("exception.user.notExists");
		}
	}

	private User getFindUserBy(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	private Series buildSeries(String title, User owner) {
		return Series.builder()
			.title(title)
			.user(owner)
			.build();
	}

}
