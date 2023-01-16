package com.prgrms.prolog.utils;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

public class TestUtils {

	public static final User USER = getUser();
	public static final Post POST = getPost();
	public static final Comment COMMENT = getComment();
	public static final String OVER_SIZE_50 = "0" + "1234567890".repeat(5);
	public static final String OVER_SIZE_100 = "0" + "1234567890".repeat(10);
	public static final String OVER_SIZE_255 = "012345" + "1234567890".repeat(25);
	public static final String OVER_SIZE_65535 = "012345" + "1234567890".repeat(6553);

	private TestUtils() {
		/* no-op */
	}

	public static User getUser() {
		return User.builder()
			.email("test@test.com")
			.nickName("테스터")
			.introduce("한줄소개")
			.prologName("블로그 제목")
			.provider("OAuth 리소스 제공자")
			.oauthId("리소스 주인 id")
			.build();
	}

	public static Post getPost() {
		return Post.builder()
			.title("제목")
			.content("내용")
			.openStatus(true)
			.user(USER)
			.build();
	}

	public static Comment getComment() {
		return Comment.builder()
			.content("내용")
			.post(POST)
			.build();
	}
}
