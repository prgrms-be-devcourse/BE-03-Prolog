package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.prolog.domain.user.service.UserService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	ResponseEntity<UserResponse> getUserProfile(
		@PathVariable Long userId
	) {
		return ResponseEntity.ok(
			userService.getUserProfile(userId)
		);
	}

	@GetMapping("/me")
	ResponseEntity<UserResponse> getMyProfile(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok(
			userService.getUserProfile(user.id())
		);
	}

	@PutMapping("/me")
	ResponseEntity<UserResponse> updateMyProfile(
		@Valid @RequestBody UpdateUserRequest updateUserRequest,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok(
			userService.updateUserProfile(updateUserRequest, user.id())
		);
	}

	@DeleteMapping("/me")
	@ResponseStatus(NO_CONTENT)
	void deleteMyProfile(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		userService.deleteUser(user.id());
	}

}
