package com.prgrms.prolog.domain.comment.repository;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Import({JpaConfig.class})
class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("댓글 번호로 조회 시에 회원을 조인해서 가져온다.")
	void joinUserByCommentIdTest() {
		// given
		User user = userRepository.save(USER);
		Post post = postRepository.save(POST);
		Comment comment = Comment.builder()
			.user(user)
			.post(post)
			.content("댓글 내용")
			.build();
		Comment savedComment = commentRepository.save(comment);
		// when
		Comment findComment = commentRepository.joinUserByCommentId(savedComment.getId());
		// then
		assertThat(findComment).isNotNull();
	}
}