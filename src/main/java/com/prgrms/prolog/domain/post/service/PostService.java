package com.prgrms.prolog.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.prolog.domain.post.dto.PostRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;

public interface PostService {
	Long create(PostRequest.CreateRequest request, Long userId);

	PostResponse findById(Long postId);

	Page<PostResponse> findAll(Pageable pageable);

	PostResponse update(PostRequest.UpdateRequest update, Long userId, Long postId);

	void delete(Long id);
}
