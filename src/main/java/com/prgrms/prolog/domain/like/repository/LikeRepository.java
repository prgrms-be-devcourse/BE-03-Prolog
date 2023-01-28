package com.prgrms.prolog.domain.like.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	Optional<Like> findByUserAndPost(User user, Post post);
}
