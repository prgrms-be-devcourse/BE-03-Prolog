package com.prgrms.prolog.domain.series.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.prolog.domain.series.dto.SeriesResponse;
import com.prgrms.prolog.domain.series.service.SeriesService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/series")
@RestController
public class SeriesController {

	private final SeriesService seriesService;

	@GetMapping
	public ResponseEntity<SeriesResponse> findSeriesByTitle(
		@RequestParam String title,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok(
			seriesService.findByTitle(user.id(), title)
		);
	}
}
