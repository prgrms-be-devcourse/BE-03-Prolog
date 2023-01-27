package com.prgrms.prolog.domain.post.repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;

@DataJpaTest
@Import({JpaConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PostRepositoryTest {

	private static final String NOT_EXIST_POST = "해당 게시물은 존재하지 않는 게시물입니다.";
	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	User user;
	Post post;

	@BeforeEach
	void setUp() {
		user = userRepository.save(USER);
		Post p = Post.builder()
			.title(TITLE)
			.content(CONTENT)
			.openStatus(true)
			.user(user)
			.build();
		post = postRepository.save(p);
	}

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save() {
		Post newPost = Post.builder()
			.title("새로 저장한 제목")
			.content("새로 저장한 내용")
			.openStatus(false)
			.user(user)
			.build();

		Post savePost = postRepository.save(newPost);

		assertThat(savePost.getId()).isNotNull();
	}

	@Test
	@DisplayName("아이디로 게시물을 단건 조회할 수 있다.")
	void findById() {

		Post findPost = postRepository.findById(post.getId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

		assertThat(findPost.getId()).isEqualTo(post.getId());
	}

	@Test
	@DisplayName("게시물을 전체 조회할 수 있다.")
	void findAll() {
		List<Post> all = postRepository.findAll();

		assertThat(all).isNotEmpty();
	}

	@Test
	@DisplayName("아이디로 게시물을 수정할 수 있다.")
	void update() {
		String changeTitle = "변경된 게시물 제목";
		String changeContent = "변경된 게시물 내용";
		boolean changeOpenStatus = true;

		post.changeTitle(changeTitle);
		post.changeContent(changeContent);
		post.changeOpenStatus(changeOpenStatus);

		Post findPost = postRepository.findById(post.getId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

		assertThat(findPost.getTitle()).isEqualTo(changeTitle);
		assertThat(findPost.getContent()).isEqualTo(changeContent);
		assertThat(findPost.isOpenStatus()).isEqualTo(changeOpenStatus);
	}

	@Test
	@DisplayName("아이디로 게시물을 삭제할 수 있다.")
	void delete() {
		postRepository.delete(post);

		Optional<Post> findPost = postRepository.findById(post.getId());

		assertThat(findPost).isEmpty();
	}
}