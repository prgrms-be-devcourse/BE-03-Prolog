package com.prgrms.prolog.domain.like.service;

import static com.prgrms.prolog.domain.like.dto.LikeDto.*;

public interface LikeService {
	Long save(likeRequest likeRequest);

	void cancel(likeRequest likeRequest);
}
