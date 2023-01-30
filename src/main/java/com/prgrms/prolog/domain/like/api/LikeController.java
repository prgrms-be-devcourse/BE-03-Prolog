package com.prgrms.prolog.domain.like.api;

import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	@ResponseStatus(NO_CONTENT)
	public void insert(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		final likeRequest likeRequest = new likeRequest(user.id(), postId);
		likeService.save(likeRequest);
	}

	@DeleteMapping
	@ResponseStatus(NO_CONTENT)
	public void delete(@RequestBody @Valid likeRequest likeRequest) {
		likeService.cancel(likeRequest);
	}
}
