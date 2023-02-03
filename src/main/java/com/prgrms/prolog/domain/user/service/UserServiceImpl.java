package com.prgrms.prolog.domain.user.service;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.global.config.MessageKeyConfig.*;

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
	private static final String USER_NOT_FOUND_EXCEPTION = messageKey().exception().user().notExists().endKey();

	private final UserRepository userRepository;

	/* [사용자 조회] 사용자 ID를 통해 등록된 유저 정보 찾아서 제공 없으면 예외 */
	@Override
	public UserResponse getUserProfile(Long userId) {
		return userRepository.findById(userId)
			.map(UserResponse::from)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_EXCEPTION));
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

	/* [프로필 수정] 사용자의 프로필을 수정합니다. 값을 덮어 쓰기 때문에 수정되지 않은 내용이어도 원본 데이터가 필요합니다.*/
	@Override
	@Transactional
	public UserResponse updateUserProfile(UpdateUserRequest updateUserRequest, Long userId) {
		return userRepository.findById(userId)
			.map(user -> user.changeUserProfile(updateUserRequest))
			.map(UserResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.update.fail"));
	}

	/* [사용자 삭제] 사용자를 삭제(소프트 딜리트 적용) */
	@Override
	@Transactional
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
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
