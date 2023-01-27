package com.prgrms.prolog.domain.like.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.prolog.domain.like.dto.LikeDto;
import com.prgrms.prolog.domain.like.dto.LikeDto.likeRequest;
import com.prgrms.prolog.domain.like.service.LikeServiceImpl;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {

	private final LikeServiceImpl likeService;

	@PostMapping(value = "/{postId}")
	public ResponseEntity<Long> insert(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		LikeDto.likeRequest request = new likeRequest(user.id(), postId);
		Long likeId = likeService.save(request);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/like/" + likeId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping
	public ResponseEntity<Void> delete(@RequestBody @Valid likeRequest likeRequest) {
		likeService.cancel(likeRequest);
		return ResponseEntity.noContent().build();
	}
}
