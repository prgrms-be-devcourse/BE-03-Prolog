package com.prgrms.prolog.domain.user.Repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.user.model.User;

class UserRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("저장된 유저 정보를 유저ID로 찾아 가져올 수 있다.")
	void saveAndFindByIdTest() {
		// given & when
		Optional<User> foundUser = userRepository.findById(savedUser.getId());
		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get())
			.usingRecursiveComparison()
			.isEqualTo(savedUser);
	}

	@Test
	@DisplayName("저장되지 않은 유저는 조회할 수 없다.")
	void findFailTest() {
		// when
		Optional<User> foundUser = userRepository.findById(UNSAVED_USER_ID);
		// then
		assertThat(foundUser).isNotPresent();
	}

	@Test
	@DisplayName("유저와 유저 태그를 조인하여 조회할 수 있다.")
	void joinUserTagFindByEmailTest() {
		// given & when
		User findUser = userRepository.joinUserTagFindByUserId(savedUser.getId());
		// then
		assertThat(findUser).isNotNull();
	}
}
