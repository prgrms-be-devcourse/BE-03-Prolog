package com.prgrms.prolog.domain.like.service;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.like.dto.LikeDto.LikeRequest;
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

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final LikeRepository likeRepository;

	@Override
	public Long save(LikeRequest likeRequest) {

		User user = getFindUserBy(likeRequest.userId());
		Post post = getFindPostBy(likeRequest.postId());

		//TODO 이미 좋아요 되어있으면 에러 반환 -> 409 Conflict 오류로 변환
		if (likeRepository.findByUserAndPost(user, post).isPresent()) {
			throw new EntityNotFoundException(messageKey().exception().like().alreadyExists().endKey());
		}

		Like like = likeRequest.from(user, post);
		Like savedLike = likeRepository.save(like);
		postRepository.addLikeCountByPostId(post.getId());
		return savedLike.getId();
	}

	@Override
	public void cancel(LikeRequest likeRequest) {

		User user = getFindUserBy(likeRequest.userId());
		Post post = getFindPostBy(likeRequest.postId());

		Like like = likeRepository.findByUserAndPost(user, post)
			.orElseThrow(() -> new EntityNotFoundException(messageKey().like().notExists().endKey()));

		likeRepository.delete(like);
		postRepository.subLikeCountByPostId(post.getId());
	}

	private User getFindUserBy(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(messageKey().exception().user().notExists().endKey()));
	}

	private Post getFindPostBy(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(messageKey().exception().user().notExists().endKey()));
	}
}
