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

	@PostMapping("/{post_id}/comments")
	public ResponseEntity save(
		@PathVariable(name = "post_id") Long postId,
		@Valid @RequestBody CreateCommentRequest request,
		@AuthenticationPrincipal JwtAuthentication jwt
	) {
		String userEmail = jwt.userEmail();
		commentService.save(request, userEmail, postId);
		return ResponseEntity.status(CREATED).build();
	}

	@PatchMapping("/{post_id}/comments/{id}")
	public ResponseEntity update(
		@PathVariable(name = "post_id") Long postId,
		@PathVariable(name = "id") Long commentId,
		@Valid @RequestBody UpdateCommentRequest request,
		@AuthenticationPrincipal JwtAuthentication jwt
	) {
		String userEmail = jwt.userEmail();
		commentService.update(request, userEmail, commentId);
		return ResponseEntity.ok().build();
	}
}
