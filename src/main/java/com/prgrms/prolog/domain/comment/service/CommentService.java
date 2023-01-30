package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

public interface CommentService {
	SingleCommentResponse createComment(CreateCommentRequest createCommentRequest, Long userId, Long postId);

	SingleCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long userId, Long postId, Long commentId);
}
