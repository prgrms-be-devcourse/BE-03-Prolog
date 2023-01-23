package com.prgrms.prolog.domain.user.api;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.prolog.domain.user.service.UserService;
import com.prgrms.prolog.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	ResponseEntity<UserInfo> myPage(
		@AuthenticationPrincipal JwtAuthentication user
	) {
		return ResponseEntity.ok(userService.findByEmail(user.userEmail()));
	}

}
