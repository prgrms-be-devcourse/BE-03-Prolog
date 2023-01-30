package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.oauth.dto.OauthUserInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

	private static final String DEFAULT_INTRODUCE = "한줄 소개를 입력해주세요.";

	private final UserRepository userRepository;

	/* [사용자 조회] 사용자 ID를 통해 등록된 유저 정보 찾아서 제공 없으면 예외 */
	@Override
	public UserResponse getUserProfile(Long userId) {
		return userRepository.findById(userId)
			.map(UserResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
	}

	/* [회원 가입] 등록된 사용자 인지 확인해서 맞는 경우 유저ID 제공, 아닌 경우 사용자 등록 */
	@Override
	@Transactional
	public Long signUp(OauthUserInfo userInfo) {
		return userRepository
			.findByProviderAndOauthId(userInfo.provider(), userInfo.oauthId())
			.map(User::getId)
			.orElseGet(() -> register(userInfo).getId());
	}

	/* [사용자 등록] 디폴트 설정 값으로 회원가입 진행 */
	private User register(OauthUserInfo userInfo) {
		return userRepository.save(
				User.builder()
					.email(userInfo.email())
					.nickName(userInfo.nickName())
					.introduce(DEFAULT_INTRODUCE)
					.prologName(userInfo.nickName() + "의 prolog")
					.provider(userInfo.provider())
					.oauthId(userInfo.oauthId())
					.profileImgUrl(userInfo.profileImgUrl())
					.build()
			);
	}
}
