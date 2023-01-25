package com.prgrms.prolog.domain.roottag.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.prgrms.prolog.domain.roottag.model.RootTag;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class RootTagRepositoryTest {

	@Autowired
	RootTagRepository rootTagRepository;

	@Test
	@DisplayName("태그 이름들로 루트 태그들을 검색한다.")
	void findByTagNamesInTest() {
		// given
		final Set<String> tagNames = Set.of("태그1", "태그2", "태그3", "태그4", "태그5");
		final Set<RootTag> tags = Set.of(
			new RootTag("태그1"),
			new RootTag("태그2"),
			new RootTag("태그3"),
			new RootTag("태그4"),
			new RootTag("태그5"));
		rootTagRepository.saveAll(tags);

		// when
		Set<RootTag> findTags = rootTagRepository.findByTagNamesIn(tagNames);

		// then
		assertThat(findTags).hasSize(5);
	}
}