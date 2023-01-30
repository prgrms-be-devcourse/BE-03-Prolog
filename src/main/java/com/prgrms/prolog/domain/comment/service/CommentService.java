package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

public interface CommentService {
	CreatedCommentResponse createComment(CreateCommentRequest createCommentRequest, Long userId, Long postId);

	Long updateComment(UpdateCommentRequest updateCommentRequest, Long userId, Long commentId);
}
