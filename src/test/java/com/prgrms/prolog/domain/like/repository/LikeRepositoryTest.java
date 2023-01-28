package com.prgrms.prolog.domain.like.repository;


import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.like.model.Like;


class LikeRepositoryTest extends RepositoryTest {


	@Test
	@DisplayName("존재하는 사용자가 존재하는 게시물을 좋아요를 할 때 좋아요가 생긴다.")
	void findByUserAndPostTest() {
		// when
		Optional<Like> actual = likeRepository.findByUserAndPost(savedUser, savedPost);

		// then
		assertThat(actual)
			.hasValueSatisfying(like -> assertThat(like.getId()).isEqualTo(savedLike.getId()));
	}
}