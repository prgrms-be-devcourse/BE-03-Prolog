package com.prgrms.prolog.domain.comment.service;

import static com.prgrms.prolog.domain.comment.dto.CommentDto.*;

public interface CommentService {
	Long save(CreateCommentRequest request, String email, Long postId);
	Long update(UpdateCommentRequest request, String email, Long commentId);
}
