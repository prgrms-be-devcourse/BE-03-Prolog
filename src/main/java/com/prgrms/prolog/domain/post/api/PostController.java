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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;
import com.prgrms.prolog.domain.post.service.PostServiceImpl;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostServiceImpl postService;

	public PostController(PostServiceImpl postService) {
		this.postService = postService;
	}

	@PostMapping()
	public ResponseEntity<Void> save(
		@Valid @RequestBody CreateRequest create,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		Long savePostId = postService.save(create, user.id());
		URI location = UriComponentsBuilder.fromUriString("/api/v1/posts/" + savePostId).build().toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> findById(@PathVariable Long id) { // 비공개 처리는?
		PostResponse findPost = postService.findById(id);
		return ResponseEntity.ok(findPost);
	}

	@GetMapping()
	public ResponseEntity<List<PostResponse>> findAll(
			@PageableDefault(size=10, page=0, sort="updatedAt", direction= Sort.Direction.DESC) Pageable pageable
	) {
		Page<PostResponse> allPost = postService.findAll(pageable);
		return ResponseEntity.ok(allPost.getContent());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PostResponse> update(
		@PathVariable Long id,
		@AuthenticationPrincipal JwtAuthentication user,
		@Valid @RequestBody UpdateRequest postRequest) {
		PostResponse update = postService.update(postRequest, user.id(), id);
		return ResponseEntity.ok(update);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		postService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
