package com.prgrms.prolog.domain.post.api;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;
import com.prgrms.prolog.domain.post.service.PostService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

	private final PostService postService;

	@PostMapping // TODO: 불필요한 괄호 지우기
	public ResponseEntity<Void> createPost(
		@Valid @RequestBody CreateRequest createRequest, // TODO: 매개변수 명도 명확하게
		@AuthenticationPrincipal JwtAuthentication user
	) {
		// TODO : Controller -> create, Service -> create, Repository -> save
		Long savePostId = postService.create(createRequest, user.id());
		URI location = UriComponentsBuilder.fromUriString("/api/v1/posts/" + savePostId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	// TODO : 명확한 id 표시
	@GetMapping("/{postId}")
	public ResponseEntity<PostResponse> getSinglePost(@PathVariable Long postId) {
		// TODO : 비공개 필터링 기능 추가
		PostResponse findPost = postService.findById(postId);
		return ResponseEntity.ok(findPost);
	}

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPost(
		@PageableDefault(size = 10, page = 0, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		// TODO : 응답의 타입을 Page로 전환
		Page<PostResponse> allPost = postService.findAll(pageable);
		return ResponseEntity.ok(allPost.getContent());
	}

	@PutMapping("/{postId}")
	public ResponseEntity<PostResponse> updatePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication user,
		@Valid @RequestBody UpdateRequest updateRequest) {
		PostResponse updatedPost = postService.update(updateRequest, user.id(), postId);
		return ResponseEntity.ok(updatedPost);
	}

	// TODO : JwtAuthentication 추가
	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
		postService.delete(postId);
		return ResponseEntity.noContent().build();
	}
}
