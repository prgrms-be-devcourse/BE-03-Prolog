package com.prgrms.prolog.domain.post.service;

import static com.prgrms.prolog.domain.post.dto.PostDto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
	Long createPost(CreatePostRequest request, Long userId);

	SinglePostResponse getSinglePost(Long userId, Long postId);

	Page<SinglePostResponse> getAllPost(Pageable pageable);

	SinglePostResponse updatePost(UpdatePostRequest update, Long userId, Long postId);

	void deletePost(Long userId, Long postId);
}
