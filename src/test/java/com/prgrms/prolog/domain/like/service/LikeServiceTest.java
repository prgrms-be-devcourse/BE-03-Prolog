package com.prgrms.prolog.domain.like.service;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.prolog.domain.like.dto.LikeDto.likeRequest;
import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.like.repository.LikeRepository;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

	@InjectMocks
	private LikeServiceImpl likeService;

	@Mock
	private LikeRepository likeRepository;

	@Mock
	private PostRepository postRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Like like;

	likeRequest likeRequest = new likeRequest(USER_ID, POST_ID);

	@Test
	@DisplayName("게시물에 좋아요를 누를 수 있다.")
	void insertLikeTest() {
		// given
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willReturn(Optional.empty());
		given(likeRepository.save(any(Like.class))).willReturn(like);
		given(like.getId()).willReturn(1L);

		// when
		Long likeId = likeService.save(likeRequest);

		// then
		then(likeRepository).should().save(any(Like.class));    // 행위 검증
		assertThat(likeId).isEqualTo(1L);    // 상태 검증
	}

	@Test
	@DisplayName("좋아요한 게시물을 좋아요 취소할 수 있다.")
	void cancelLikeTest() {
		// given
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willReturn(Optional.of(like));

		// when
		likeService.cancel(likeRequest);

		// then
		then(likeRepository).should().delete(any(Like.class));
	}

	@Test
	@DisplayName("좋아요한 게시물에 또 좋아요를 할 수 없다.")
	void insertDuplicateLikeTest() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willReturn(Optional.of(LIKE));

		assertThatThrownBy(() -> likeService.save(likeRequest)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("좋아요를 하지 않은 게시물에는 좋아요를 취소할 수 없다.")
	void cancelDuplicateLikeTest() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willThrow(EntityNotFoundException.class);

		assertThatThrownBy(() -> likeService.save(likeRequest)).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("좋아요를 누르면 게시물의 총 좋아요의 개수가 1씩 증가한다.")
	void addLikeCountTest() {
		// given
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willReturn(Optional.empty());
		given(likeRepository.save(any(Like.class))).willReturn(like);
		given(postRepository.addLikeCountByPostId(any())).willReturn(1);
		given(like.getId()).willReturn(1L);

		// when
		likeService.save(likeRequest);

		// then
		then(postRepository).should().addLikeCountByPostId(any());    // 행위 검증
		assertThat(postRepository.addLikeCountByPostId(POST_ID)).isEqualTo(1);
	}

	@Test
	@DisplayName("좋아요를  게시물의 총 좋아요의 개수가 1씩 증가한다.")
	void cancelLikeCountTest() {
		// given
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(POST));
		given(likeRepository.findByUserAndPost(USER, POST)).willReturn(Optional.of(LIKE));
		willDoNothing().given(likeRepository).delete(any(Like.class));
		given(postRepository.subLikeCountByPostId(any())).willReturn(1);

		// when
		likeService.cancel(likeRequest);

		// then
		then(postRepository).should().subLikeCountByPostId(any());
		assertThat(postRepository.subLikeCountByPostId(POST_ID)).isEqualTo(1);
	}
}