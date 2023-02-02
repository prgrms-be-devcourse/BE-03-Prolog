package com.prgrms.prolog.domain.series.api;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.prolog.domain.series.service.SeriesService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class SeriesController {

	private final SeriesService seriesService;

	private static final String SERIES_BASE_URI = "/api/v1/user";

	@PostMapping("/series")
	public ResponseEntity<Void> createNewSeries(
		@Valid @RequestParam CreateSeriesRequest createSeriesRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		Long seriesId = seriesService.createSeries(createSeriesRequest, user.id());
		URI location = URI.create(SERIES_BASE_URI + "/" + user.id() + "/series/" + seriesId);
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{userId}/series/{seriesId}")
	public ResponseEntity<SeriesResponse> getSingleSeries(
		@PathVariable @PositiveOrZero Long userId,
		@PathVariable @PositiveOrZero Long seriesId
	) {
		return ResponseEntity.ok(
			seriesService.getSeriesById(userId, seriesId)
		);
	}

	@GetMapping("/{userId}/series")
	public ResponseEntity<List<SeriesSimpleResponse>> getAllSeries(
		@PathVariable @PositiveOrZero Long userId
	) {
		return ResponseEntity.ok(
			seriesService.getAllSeries(userId)
		);
	}

	@PutMapping("/series/{seriesId}")
	public ResponseEntity<SeriesResponse> updateSeries(
		@Valid @RequestParam UpdateSeriesRequest updateSeriesRequest,
		@PathVariable @PositiveOrZero Long seriesId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok(
			seriesService.updateSeries(updateSeriesRequest, user.id(), seriesId)
		);
	}

	@DeleteMapping("/series/{seriesId}")
	@ResponseStatus(NO_CONTENT)
	public void deleteSeries(
		@PathVariable @PositiveOrZero Long seriesId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		seriesService.deleteSeries(user.id(), seriesId);
	}
}
