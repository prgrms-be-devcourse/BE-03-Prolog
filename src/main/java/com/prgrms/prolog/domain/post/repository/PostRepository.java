package com.prgrms.prolog.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.prolog.domain.post.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
