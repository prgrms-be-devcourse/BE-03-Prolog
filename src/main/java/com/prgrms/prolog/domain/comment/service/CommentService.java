package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

public interface CommentService {
	Long save(CreateCommentRequest request, Long userId, Long postId);
	Long update(UpdateCommentRequest request, Long userId, Long commentId);
}
