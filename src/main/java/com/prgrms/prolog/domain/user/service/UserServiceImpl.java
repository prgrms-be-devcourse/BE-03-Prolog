package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private static final String DEFAULT_INTRODUCE = "한줄 소개를 입력해주세요.";

	private final UserRepository userRepository;

	/* [사용자 조회] 이메일 값으로 등록된 유저 정보 찾아서 제공 */
	@Override
	public UserInfo findByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(UserInfo::new)
			.orElseThrow(IllegalArgumentException::new);
	}

	/* [로그인] 등록된 사용자인지 확인해서 맞는 경우 정보 제공, 아닌 경우 등록 진행 */
	@Transactional
	public UserInfo login(UserProfile userProfile) {
		return userRepository.findByEmail(userProfile.email())
			.map(UserInfo::new)
			.orElseGet(() -> register(userProfile));
	}

	/* [사용자 등록] 디폴트 설정 값으로 회원가입 진행 */
	private UserInfo register(UserProfile userProfile) {
		return new UserInfo(
			userRepository.save(
				User.builder()
					.email(userProfile.email())
					.nickName(userProfile.nickName())
					.introduce(DEFAULT_INTRODUCE)
					.prologName(userProfile.nickName() + "의 prolog")
					.provider(userProfile.provider())
					.oauthId(userProfile.oauthId())
					.build()
			)
		);
	}
}
