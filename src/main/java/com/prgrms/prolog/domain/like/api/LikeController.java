package com.prgrms.prolog.domain.like.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.prolog.domain.like.dto.LikeDto.likeRequest;
import com.prgrms.prolog.domain.like.service.LikeServiceImpl;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
@RestController
public class LikeController {

	private final LikeServiceImpl likeService;

	@PostMapping(value = "/{postId}")
	public ResponseEntity<Void> insert(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		final likeRequest likeRequest = new likeRequest(user.id(), postId);
		Long likeId = likeService.save(likeRequest);
		URI location = UriComponentsBuilder.fromUriString("/api/v1/like/" + postId + "/" + likeId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> delete(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		final likeRequest likeRequest = new likeRequest(user.id(), postId);
		likeService.cancel(likeRequest);
		return ResponseEntity.noContent().build();
	}
}
