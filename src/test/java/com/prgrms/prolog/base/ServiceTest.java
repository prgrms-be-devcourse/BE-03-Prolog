package com.prgrms.prolog.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.like.repository.LikeRepository;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

	@Mock
	protected UserRepository userRepository;

	@Mock
	protected PostRepository postRepository;

	@Mock
	protected SeriesRepository seriesRepository;

	@Mock
	protected LikeRepository likeRepository;

	@Mock
	protected User user;

	@Mock
	protected Post post;

	@Mock
	protected Series series;

	@Mock
	protected Like like;

}
