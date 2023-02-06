package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import com.prgrms.prolog.global.oauth.dto.OauthUserInfo;

public interface UserService {

	/* 사용자 회원 가입 */
	Long signUp(OauthUserInfo oauthUserInfo);

	/* 사용자 프로필 조회 */
	UserResponse getUserProfile(Long userId);

	/* 사용자 프로필 수정 */
	UserResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId);

	/* 사용자 삭제 */
	void deleteUser(Long userId);

}
