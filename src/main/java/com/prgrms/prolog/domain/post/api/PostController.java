package com.prgrms.prolog.domain.post.api;

import static com.prgrms.prolog.domain.post.dto.PostDto.*;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.prolog.domain.post.service.PostService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<Void> createPost(
		@Valid @RequestBody CreatePostRequest createPostRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		Long savePostId = postService.createPost(createPostRequest, user.id());
		URI location = UriComponentsBuilder.fromUriString("/api/v1/posts/" + savePostId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<SinglePostResponse> getSinglePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		// TODO : 비공개 필터링 기능 추가
		SinglePostResponse findPost = postService.getSinglePost(user.id(), postId);
		return ResponseEntity.ok(findPost);
	}

	@GetMapping
	public ResponseEntity<List<SinglePostResponse>> getAllPost(
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		// TODO : 응답의 타입을 Page로 전환
		Page<SinglePostResponse> allPost = postService.getAllPost(pageable);
		return ResponseEntity.ok(allPost.getContent());
	}

	@PutMapping("/{postId}")
	public ResponseEntity<SinglePostResponse> updatePost(
		@PathVariable Long postId,
		@Valid @RequestBody UpdatePostRequest updateRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		SinglePostResponse updatedPost = postService.updatePost(updateRequest, user.id(), postId);
		return ResponseEntity.ok(updatedPost);
	}

	@DeleteMapping("/{postId}")
	@ResponseStatus(NO_CONTENT)
	public void deletePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		postService.deletePost(user.id(), postId);
	}
}
