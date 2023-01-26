package com.prgrms.prolog.domain.series.repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
@Transactional
public class SeriesRepositoryTest {

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private UserRepository userRepository;

	private Series savedSeries;
	private User savedUser;

	@BeforeEach
	void setUp() {
		savedUser = userRepository.save(USER);
		Series series = Series.builder()
			.title(SERIES_TITLE)
			.user(savedUser)
			.build();
		savedSeries = seriesRepository.save(series);
	}

	@Test
	@DisplayName("해당 유저가 가진 시리즈 중에서 찾는 제목의 시리즈를 조회한다.")
	void findByIdAndTitleTest() {
		// given & when
		Optional<Series> series = seriesRepository.findByIdAndTitle(savedUser.getId(), SERIES_TITLE);
		// then
		assertThat(series).isPresent();
	}

	@Disabled
	@Test
	@DisplayName("포스트 조회시 N+1 테스트")
	void nPlus1Test() {
		// given & when
		Optional<Series> series = seriesRepository.findByIdAndTitle(savedUser.getId(), SERIES_TITLE);
		// then
		assertThat(series).isPresent();
	}
}
