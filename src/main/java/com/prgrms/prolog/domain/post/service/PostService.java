package com.prgrms.prolog.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.prolog.domain.post.dto.PostRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;

public interface PostService {
		public Long save(PostRequest.CreateRequest request, Long userId);
		public PostResponse findById(Long postId);
		public Page<PostResponse> findAll(Pageable pageable);
		public PostResponse update(PostRequest.UpdateRequest update, Long userId, Long postId);
		public void delete(Long id);
}
