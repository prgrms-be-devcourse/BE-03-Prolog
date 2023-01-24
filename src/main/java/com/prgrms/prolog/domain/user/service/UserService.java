package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

public interface UserService {

	/* 사용자 회원 가입 */
	IdResponse signUp(UserInfo userInfo);

	/* 사용자 조회 */
	UserProfile findUserProfileByUserId(Long userId);

}

