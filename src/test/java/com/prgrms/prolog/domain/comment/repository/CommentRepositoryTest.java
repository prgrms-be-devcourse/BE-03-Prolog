package com.prgrms.prolog.domain.comment.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.prolog.base.RepositoryTest;
import com.prgrms.prolog.domain.comment.model.Comment;

class CommentRepositoryTest extends RepositoryTest {

	@Test
	@DisplayName("댓글 번호로 조회 시에 회원을 조인해서 가져온다.")
	void joinUserByCommentIdTest() {
		// given & when
		Comment findComment = commentRepository.joinUserByCommentId(savedComment.getId());
		// then
		assertThat(findComment).isNotNull();
	}
}