package com.prgrms.prolog.domain.user.Repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.DatabaseConfig;
import com.prgrms.prolog.global.config.JpaConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({DatabaseConfig.class, JpaConfig.class})
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("정상적으로 DB에 저장이 된다.")
	void saveTest() {
		// given & when & then
		assertDoesNotThrow(() -> userRepository.save(getUser()));
	}

	@Test
	@DisplayName("저장된 유저 정보를 유저ID로 찾아 가져올 수 있다.")
	void saveAndFindByIdTest() {
		// given
		User savedUser = userRepository.save(getUser());
		// when
		Optional<User> foundUser = userRepository.findById(savedUser.getId());
		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get())
			.usingRecursiveComparison()
			.isEqualTo(savedUser);

	}

	@Test
	@DisplayName("이메일로 저장된 유저 정보를 조회할 수 있다.")
	void findEmailTest() {
		// given
		User savedUser = userRepository.save(getUser());
		// when
		Optional<User> foundUser = userRepository.findByEmail(savedUser.getEmail());
		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get())
			.usingRecursiveComparison()
			.isEqualTo(savedUser);
	}

	@Test
	@DisplayName("저장되지 않은 유저는 조회할 수 없다.")
	void findFailTest() {
		// given
		User notSavedUser = getUser();
		// when
		Optional<User> foundUser = userRepository.findByEmail(notSavedUser.getEmail());
		// then
		assertThat(foundUser).isNotPresent();
	}
}
