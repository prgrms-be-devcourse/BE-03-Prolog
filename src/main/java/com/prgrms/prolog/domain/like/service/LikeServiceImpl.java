package com.prgrms.prolog.domain.like.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.like.dto.LikeDto.likeRequest;
import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.like.repository.LikeRepository;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Override
	public Long save(likeRequest likeRequest) {

		User user = getFindUserBy(likeRequest.userId());
		Post post = getFindPostBy(likeRequest.postId());

		//TODO 이미 좋아요 되어있으면 에러 반환 -> 409 Conflict 오류로 변환
		if (likeRepository.findByUserAndPost(user, post).isPresent()) {
			throw new EntityNotFoundException("exception.like.alreadyExist");
		}

		Like like = likeRepository.save(saveLike(user, post));

		postRepository.addLikeCount(post.getId());
		return like.getId();
	}

	@Override
	public void cancel(likeRequest likeRequest) {

		User user = getFindUserBy(likeRequest.userId());
		Post post = getFindPostBy(likeRequest.postId());

		Like like = likeRepository.findByUserAndPost(user, post)
			.orElseThrow(() -> new EntityNotFoundException("exception.like.notExist"));

		likeRepository.delete(like);
		postRepository.subLikeCount(post.getId());
	}

	private Like saveLike(User user, Post post) {
		return Like.builder()
			.user(user)
			.post(post)
			.build();
	}

	private User getFindUserBy(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	private Post getFindPostBy(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("exception.post.notExists"));
	}
}
