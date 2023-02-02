package com.prgrms.prolog.domain.comment.api;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.prolog.domain.comment.service.CommentService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<SingleCommentResponse> createComment(
		@PathVariable(name = "postId") Long postId,
		@Valid @RequestBody CreateCommentRequest createCommentRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		SingleCommentResponse singleCommentResponse
			= commentService.createComment(createCommentRequest, user.id(), postId);
		return ResponseEntity.status(CREATED).body(singleCommentResponse);
	}

	@PatchMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<SingleCommentResponse> updateComment(
		@PathVariable(name = "postId") Long postId,
		@PathVariable(name = "commentId") Long commentId,
		@Valid @RequestBody UpdateCommentRequest updateCommentRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		SingleCommentResponse singleCommentResponse
			= commentService.updateComment(updateCommentRequest, user.id(), postId, commentId);
		return ResponseEntity.ok(singleCommentResponse);
	}
}
