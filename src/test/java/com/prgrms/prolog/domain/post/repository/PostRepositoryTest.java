package com.prgrms.prolog.domain.post.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.post.model.Post;

class PostRepositoryTest extends RepositoryTest {

	private static final String NOT_EXIST_POST = "해당 게시물은 존재하지 않는 게시물입니다.";

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save() {
		Post newPost = Post.builder()
			.title("새로 저장한 제목")
			.content("새로 저장한 내용")
			.openStatus(false)
			.user(savedUser)
			.build();

		Post savePost = postRepository.save(newPost);

		assertThat(savePost.getId()).isNotNull();
	}

	@Test
	@DisplayName("아이디로 게시물을 단건 조회할 수 있다.")
	void findById() {

		Post findPost = postRepository.findById(savedPost.getId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

		assertThat(findPost.getId()).isEqualTo(savedPost.getId());
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

		savedPost.changeTitle(changeTitle);
		savedPost.changeContent(changeContent);
		savedPost.changeOpenStatus(changeOpenStatus);

		Post findPost = postRepository.findById(savedPost.getId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

		assertThat(findPost.getTitle()).isEqualTo(changeTitle);
		assertThat(findPost.getContent()).isEqualTo(changeContent);
		assertThat(findPost.isOpenStatus()).isEqualTo(changeOpenStatus);
	}

	@Test
	@DisplayName("아이디로 게시물을 삭제할 수 있다.")
	void delete() {
		postRepository.delete(savedPost);

		Optional<Post> findPost = postRepository.findById(savedPost.getId());

		assertThat(findPost).isEmpty();
	}
}