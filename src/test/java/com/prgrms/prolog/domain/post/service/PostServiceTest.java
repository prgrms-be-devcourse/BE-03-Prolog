package com.prgrms.prolog.domain.post.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.post.dto.PostDto.CreatePostRequest;
import com.prgrms.prolog.domain.post.dto.PostDto.SinglePostResponse;
import com.prgrms.prolog.domain.post.dto.PostDto.UpdatePostRequest;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
class PostServiceTest {

	@Autowired
	PostService postService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	SeriesRepository seriesRepository;

	User user;
	Post post;
	Series savedSeries;

	@BeforeEach
	void setData() {
		user = userRepository.save(USER);
		Series series = Series.builder().title(SERIES_TITLE).user(user).build();
		savedSeries = seriesRepository.save(series);
		post = Post.builder()
			.title("테스트 게시물")
			.content("테스트 내용")
			.openStatus(true)
			.user(user)
			.build();
		post.setSeries(savedSeries);
		postRepository.save(post);
	}

	@Test
	@DisplayName("게시물을 등록할 수 있다.")
	void save_success() {
		final CreatePostRequest postRequest = new CreatePostRequest("테스트", "테스트 내용", "#테스트", true, SERIES_TITLE);
		Long savePostId = postService.createPost(postRequest, user.getId());
		assertThat(savePostId).isNotNull();
	}

	@Test
	@DisplayName("게시글에 태그 없이 등록할 수 있다.")
	void savePostAndWithOutAnyTagTest() {
		// given
		final CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용", null, true, SERIES_TITLE);

		// when
		Long savedPostId = postService.createPost(request, user.getId());
		SinglePostResponse findPostResponse = postService.getSinglePost(user.getId(), savedPostId);
		Set<String> findTags = findPostResponse.tags();

		// then
		assertThat(findTags).isEmpty();
	}

	@Test
	@DisplayName("게시글에 태그가 공백이거나 빈 칸이라면 태그는 무시된다.")
	void savePostWithBlankTagTest() {
		// given
		final CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용", "# #", true, SERIES_TITLE);

		// when
		Long savedPostId = postService.createPost(request, user.getId());
		SinglePostResponse findPostResponse = postService.getSinglePost(user.getId(), savedPostId);
		Set<String> findTags = findPostResponse.tags();

		// then
		assertThat(findTags).isEmpty();
	}

	@Test
	@DisplayName("게시글에 복수의 태그를 등록할 수 있다.")
	void savePostAndTagsTest() {
		// given
		final CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용", "#테스트#test#test1#테 스트", true,
			SERIES_TITLE);
		final List<String> expectedTags = List.of("테스트", "test", "test1", "테 스트");

		// when
		Long savedPostId = postService.createPost(request, user.getId());
		SinglePostResponse findPostResponse = postService.getSinglePost(user.getId(), savedPostId);
		Set<String> findTags = findPostResponse.tags();

		// then
		assertThat(findTags)
			.containsExactlyInAnyOrderElementsOf(expectedTags);
	}

	@Test
	@DisplayName("게시물과 태그를 조회할 수 있다.")
	void findPostAndTagsTest() {
		// given
		final CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용", "#테스트", true, SERIES_TITLE);

		// when
		Long savedPostId = postService.createPost(request, user.getId());
		SinglePostResponse findPost = postService.getSinglePost(user.getId(), savedPostId);

		// then
		assertThat(findPost)
			.hasFieldOrPropertyWithValue("title", request.title())
			.hasFieldOrPropertyWithValue("content", request.content())
			.hasFieldOrPropertyWithValue("openStatus", request.openStatus())
			.hasFieldOrPropertyWithValue("tags", Set.of("테스트"));
	}

	@Test
	@DisplayName("존재하지 않는 사용자(비회원)의 이메일로 게시물을 등록할 수 없다.")
	void save_fail() {
		CreatePostRequest postRequest = new CreatePostRequest("테스트", "테스트 내용", "#테스트", true, SERIES_TITLE);

		assertThatThrownBy(() -> postService.createPost(postRequest, UNSAVED_USER_ID))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 단건 조회할 수 있다")
	void findById() {
		SinglePostResponse findPost = postService.getSinglePost(user.getId(), post.getId());

		assertThat(findPost.title()).isEqualTo(post.getTitle());
		assertThat(findPost.content()).isEqualTo(post.getContent());
		assertThat(findPost.openStatus()).isEqualTo(post.isOpenStatus());
		assertThat(findPost.user().id()).isEqualTo(post.getUser().getId());
		assertThat(findPost.commentCount()).isEqualTo(post.getComments().size());
	}

	@Test
	@DisplayName("존재하지 않는 게시물의 아이디로 게시물을 조회할 수 없다.")
	void findById_fail() {
		assertThatThrownBy(() -> postService.getSinglePost(user.getId(), 0L)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하는 게시물의 아이디로 게시물의 제목, 내용, 태그, 공개범위를 수정할 수 있다.")
	void update_success() {
		final CreatePostRequest createRequest = new CreatePostRequest("테스트 제목", "테스트 내용", "#테스트#test#test1#테 스트", true,
			SERIES_TITLE);
		Long savedPost = postService.createPost(createRequest, user.getId());

		final UpdatePostRequest updateRequest = new UpdatePostRequest("수정된 테스트", "수정된 테스트 내용", "#테스트#수정된 태그", true);
		SinglePostResponse updatedPostResponse = postService.updatePost(updateRequest, user.getId(), savedPost);

		assertThat(updatedPostResponse)
			.hasFieldOrPropertyWithValue("title", updateRequest.title())
			.hasFieldOrPropertyWithValue("content", updateRequest.content())
			.hasFieldOrPropertyWithValue("tags", Set.of("테스트", "수정된 태그"));
	}

	@Test
	@DisplayName("존재하지 않는 게시물의 아이디로 게시물을 수정할 수 없다.")
	void update_fail() {
		UpdatePostRequest updateRequest = new UpdatePostRequest("수정된 테스트", "수정된 테스트 내용", "", true);

		assertThatThrownBy(() -> postService.updatePost(updateRequest, user.getId(), 0L))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시물을 전체 조회할 수 있다.")
	void findAll_success() {
		PageRequest page = PageRequest.of(0, 10);

		Page<SinglePostResponse> all = postService.getAllPost(page);

		assertThat(all).isNotNull();
	}

	@Test
	@DisplayName("게시물 아이디로 게시물을 삭제할 수 있다.")
	void delete_success() {
		postService.deletePost(user.getId(), post.getId());

		Optional<Post> findPost = postRepository.findById(post.getId());

		assertThat(findPost).isEmpty();
	}

	@Test
	@DisplayName("존재하지 않는 게시물 아이디 입력시 게시물을 삭제할 수 없다.")
	void delete_fail() {
		assertThatThrownBy(() ->
			postService.deletePost(user.getId(), 0L)).isInstanceOf(IllegalArgumentException.class);
	}
}