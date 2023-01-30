package com.prgrms.prolog.domain.like.service;

import com.prgrms.prolog.domain.like.dto.LikeDto;

public interface LikeService {
	Long save(LikeDto.likeRequest likeRequest);

	void cancel(LikeDto.likeRequest likeRequest);
}
