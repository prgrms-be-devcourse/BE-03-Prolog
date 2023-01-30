package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

public interface CommentService {
	Long save(CreateCommentRequest createCommentRequest, Long userId, Long postId);
	Long update(UpdateCommentRequest updateCommentRequest, Long userId, Long commentId);
}
