package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

public interface UserService {

	/* 사용자 로그인 */
	UserInfo login(UserProfile userProfile);

	/* 이메일로 사용자 조회 */
	UserInfo findByEmail(String email);

}

