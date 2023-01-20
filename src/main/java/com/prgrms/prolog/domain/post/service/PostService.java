package com.prgrms.prolog.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@Service
@Transactional
public class PostService {

	private static final String POST_NOT_EXIST_MESSAGE = "존재하지 않는 게시물입니다.";
	private static final String USER_NOT_EXIST_MESSAGE = "존재하지 않는 사용자입니다.";

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	public Long save(CreateRequest create, String userEmail) {
		User user = userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST_MESSAGE));
		Post post = postRepository.save(CreateRequest.toEntity(create, user));
		return post.getId();
	}

	@Transactional(readOnly = true)
	public PostResponse findById(Long id) {
		return postRepository.findById(id)
			.map(PostResponse::toPostResponse)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
	}

	@Transactional(readOnly = true)
	public Page<PostResponse> findAll(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(PostResponse::toPostResponse);
	}

	public PostResponse update(Long id, UpdateRequest update) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
		post.changePost(update);
		return PostResponse.toPostResponse(post);
	}

	public void delete(Long id) {
		Post findPost = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
		postRepository.delete(findPost);
	}
}