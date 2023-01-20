package com.prgrms.prolog.domain.post.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.DatabaseConfig;

@Import(DatabaseConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
class PostServiceTest {

	@Autowired
	private PostService postService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	User user;
	Post post;

	@BeforeEach
	void setData() {
		user = userRepository.save(USER);
		post = Post.builder()
			.title("테스트 게시물")
			.content("테스트 내용")
			.openStatus(true)
			.user(user)
			.build();
		postRepository.save(post);
	}

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save_success() {
		CreateRequest postRequest = new CreateRequest("테스트", "테스트 내용", true);
		Long savePostId = postService.save(postRequest, USER_EMAIL);
		assertThat(savePostId).isNotNull();
	}

	@Test
	@DisplayName("존재하지 않는 사용자(비회원)의 이메일로 게시물을 등록할 수 없다.")
	void save_fail() {
		String notExistEmail = "no_email@test.com";

		CreateRequest postRequest = new CreateRequest("테스트", "테스트 내용", true);

		assertThatThrownBy(() -> postService.save(postRequest, notExistEmail))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 단건 조회할 수 있다")
	void findById() {
		PostResponse findPost = postService.findById(post.getId());

		assertThat(findPost.title()).isEqualTo(post.getTitle());
		assertThat(findPost.content()).isEqualTo(post.getContent());
		assertThat(findPost.openStatus()).isEqualTo(post.isOpenStatus());
		assertThat(findPost.user().id()).isEqualTo(post.getUser().getId());
		assertThat(findPost.commentCount()).isEqualTo(post.getComments().size());
	}

	@Test
	@DisplayName("존재하지 않는 게시물의 아이디로 게시물을 조회할 수 없다.")
	void findById_fail() {
		assertThatThrownBy(() -> postService.findById(0L)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하는 게시물의 아이디로 게시물을 수정할 수 있다.")
	void update_success() {
		UpdateRequest updateRequest = new UpdateRequest("수정된 테스트", "수정된 테스트 내용", true);

		PostResponse update = postService.update(post.getId(), updateRequest);

		assertThat(update.title()).isEqualTo("수정된 테스트");
		assertThat(update.content()).isEqualTo("수정된 테스트 내용");
	}

	@Test
	@DisplayName("존재하지 않는 게시물의 아이디로 게시물을 수정할 수 없다.")
	void update_fail() {
		UpdateRequest updateRequest = new UpdateRequest("수정된 테스트", "수정된 테스트 내용", true);

		assertThatThrownBy(() -> postService.update(0L, updateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시물을 전체 조회할 수 있다.")
	void findAll_success() {
		PageRequest page = PageRequest.of(0, 10);

		Page<PostResponse> all = postService.findAll(page);

		assertThat(all).hasSize(1);
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 삭제할 수 있다.")
	void delete_success() {
		postService.delete(post.getId());

		Optional<Post> findPost = postRepository.findById(post.getId());

		assertThat(findPost).isEmpty();
	}

	@Test
	@DisplayName("존재하지 않는 게시물 아이디 입력시 게시물을 삭제할 수 없다.")
	void delete_fail() {
		assertThatThrownBy(() ->
			postService.delete(0L)).isInstanceOf(IllegalArgumentException.class);
	}
}