package com.prgrms.prolog.domain.like.repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Import({JpaConfig.class})
class LikeRepositoryTest {

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("존재하는 사용자가 존재하는 게시물을 좋아요를 할 때 좋아요가 생긴다.")
	void findByUserAndPostTest() {
		// given
		User savedUser = userRepository.save(USER);
		Post post = Post.builder()
			.title(TITLE)
			.content(CONTENT)
			.openStatus(true)
			.user(savedUser)
			.build();
		Post savedPost = postRepository.save(post);

		Like like = Like.builder()
			.user(savedUser)
			.post(savedPost)
			.build();
		likeRepository.save(like);

		// when
		Optional<Like> actual = likeRepository.findByUserAndPost(savedUser, savedPost);

		// then
		assertThat(actual)
			.hasValueSatisfying(l -> assertThat(l.getId()).isEqualTo(1L));
	}
}