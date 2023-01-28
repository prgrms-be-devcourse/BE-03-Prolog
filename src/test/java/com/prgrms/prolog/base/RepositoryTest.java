package com.prgrms.prolog.base;

import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.prolog.config.TestContainerConfig;
import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.comment.repository.CommentRepository;
import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.like.repository.LikeRepository;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.roottag.repository.RootTagRepository;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.config.JpaConfig;

@AutoConfigureTestDatabase(replace = NONE)
@Import({JpaConfig.class, TestContainerConfig.class})
@DataJpaTest
public abstract class RepositoryTest {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected PostRepository postRepository;

	@Autowired
	protected CommentRepository commentRepository;

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected LikeRepository likeRepository;

	@Autowired
	protected RootTagRepository rootTagRepository;

	protected User savedUser;
	protected Post savedPost;
	protected Comment savedComment;
	protected Series savedSeries;
	protected Like savedLike;

	@BeforeEach
	void setUpEntity() {
		// 유저
		User user = User.builder()
			.email(USER_EMAIL)
			.nickName(USER_NICK_NAME)
			.introduce(USER_INTRODUCE)
			.prologName(USER_PROLOG_NAME)
			.provider(PROVIDER)
			.oauthId(OAUTH_ID)
			.profileImgUrl(USER_PROFILE_IMG_URL)
			.build();
		savedUser = userRepository.save(user);

		//게시글
		Post post = Post.builder()
			.title(POST_TITLE)
			.content(POST_CONTENT)
			.openStatus(true)
			.user(savedUser)
			.build();
		savedPost = postRepository.save(post);

		// 댓글
		Comment comment = Comment.builder()
			.user(savedUser)
			.post(savedPost)
			.content(COMMENT_CONTENT)
			.build();
		savedComment = commentRepository.save(comment);

		// 시리즈
		Series series = Series.builder()
			.title(SERIES_TITLE)
			.user(savedUser)
			.build();
		savedSeries = seriesRepository.save(series);
		post.setSeries(savedSeries);

		// 좋아요
		Like like = Like.builder()
			.user(savedUser)
			.post(savedPost)
			.build();
		savedLike = likeRepository.save(like);
	}
}
