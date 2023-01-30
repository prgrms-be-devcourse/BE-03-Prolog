package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.comment.repository.CommentRepository;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.user.dto.UserDto;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public Long save(CreateCommentRequest createCommentRequest, Long userId, Long postId) {
		User findUser = getFindUserBy(userId);
		Post findPost = getFindPostBy(postId);

		Comment comment = CreateCommentRequest.toEntity(createCommentRequest, findUser, findPost);

		// TODO : return savedComment
		return commentRepository.save(comment).getId();
	}

	@Override
	@Transactional
	public Long update(UpdateCommentRequest updateCommentRequest, Long userId, Long commentId) {
		Comment findComment = commentRepository.joinUserByCommentId(commentId);
		validateCommentNotNull(findComment);
		findComment.changeContent(updateCommentRequest.content());
		return findComment.getId();
	}

	private User getFindUserBy(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("exception.user.notExists"));
	}

	private Post getFindPostBy(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("exception.post.notExists"));
	}

	private void validateCommentNotNull(Comment comment) {
		Assert.notNull(comment, "exception.comment.notExists");
	}
}
