package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

import java.util.Objects;

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
	public SingleCommentResponse createComment(CreateCommentRequest createCommentRequest, Long userId, Long postId) {
		User findUser = getFindUserBy(userId);
		Post findPost = getFindPostBy(postId);
		Comment comment = CreateCommentRequest.from(createCommentRequest, findUser, findPost);
		Comment savedComment = commentRepository.save(comment);
		return SingleCommentResponse.from(UserDto.UserResponse.from(findUser), savedComment.getContent());
	}

	@Override
	@Transactional
	public SingleCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long userId, Long postId, Long commentId) {
		Post findPost = getFindPostBy(postId);
		User findUser = getFindUserBy(userId);
		Comment findComment = commentRepository.joinUserByCommentId(commentId);

		validatePostComment(findPost, findComment);
		validateUserComment(findUser, findComment);

		validateCommentNotNull(findComment);
		findComment.changeContent(updateCommentRequest.content());

		return SingleCommentResponse.from(UserDto.UserResponse.from(findUser), updateCommentRequest.content());
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

	private void validateUserComment(User findUser, Comment findComment) {
		Assert.isTrue(!Objects.equals(findComment.getUser().getId(), findUser.getId()),
			"exception.comment.user.notSame");
	}

	private void validatePostComment(Post findPost, Comment findComment) {
		Assert.isTrue(!Objects.equals(findPost.getId(), findComment.getPost().getId()),
			"exception.comment.post.notSame");
	}
}
