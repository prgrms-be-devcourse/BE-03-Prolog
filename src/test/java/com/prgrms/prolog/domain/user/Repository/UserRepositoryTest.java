package com.prgrms.prolog.domain.user.Repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({JpaConfig.class})
@Transactional
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User savedUser;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(getUser());
	}

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
		// given
		Long unsavedUserId = 0L;
		// when
		Optional<User> foundUser = userRepository.findById(unsavedUserId);
		// then
		assertThat(foundUser).isNotPresent();
	}
}
