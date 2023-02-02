package com.prgrms.prolog.domain.like.service;

import static com.prgrms.prolog.domain.like.dto.LikeDto.*;

public interface LikeService {
	Long save(LikeRequest likeRequest);

	void cancel(LikeRequest likeRequest);
}
