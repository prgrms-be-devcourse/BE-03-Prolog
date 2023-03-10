package com.prgrms.prolog.utils;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.like.model.Like;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.roottag.model.RootTag;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.user.dto.UserDto.UserResponse;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.oauth.dto.KakaoUserInfo;
import com.prgrms.prolog.global.oauth.dto.OauthUserInfo;

public class TestUtils {

	// User
	public static final Long USER_ID = 1L;
	public static final User USER = getUser();
	public static final Long UNSAVED_USER_ID = 0L;
	public static final String USER_EMAIL = "dev@programmers.com";
	public static final String USER_NICK_NAME = "머쓱이";
	public static final String USER_INTRODUCE = "머쓱이에욤";
	public static final String USER_PROLOG_NAME = "머쓱이의 prolog";
	public static final String PROVIDER = "kakao";
	public static final String OAUTH_ID = "kakao@123456789";
	public static final String USER_PROFILE_IMG_URL = "http://kakao/defaultImg.jpg";
	public static final OauthUserInfo USER_INFO = getUserInfo();
	public static final UserResponse USER_PROFILE = getUserProfile();
	public static final String USER_ROLE = "ROLE_USER";

	// Post
	public static final Long POST_ID = 1L;
	public static final Post POST = getPost();
	public static final String TITLE = "제목을 입력해주세요";
	public static final String CONTENT = "내용을 입력해주세요";
	public static final String POST_TITLE = "게시글 제목";
	public static final String POST_CONTENT = "게시글 내용";
	public static final String UPDATE_TITLE = "수정할 제목을 입력해주세요";
	public static final String UPDATE_CONTENT = "수정할 내용을 입력해주세요";

	// Comment
	public static final Long COMMENT_ID = 1L;
	public static final Comment COMMENT = getComment();
	public static final String COMMENT_CONTENT = "댓글 내용";

	// Series
	public static final Series SERIES = getSeries();
	public static final String SERIES_TITLE = "시리즈 제목";

	// Like
	public static final Long LIKE_ID = 1L;
	public static final Like LIKE = getLike();

	// RootTag & PostTag Data
	public static final String ROOT_TAG_NAME = "머쓱 태그";
	public static final Integer POST_TAG_COUNT = 0;
	public static final RootTag ROOT_TAG = getRootTag();
	public static final PostTag POST_TAG = getPostTag();

	// Over Size String Dummy
	public static final String OVER_SIZE_50 = "0" + "1234567890".repeat(5);
	public static final String OVER_SIZE_100 = "0" + "1234567890".repeat(10);
	public static final String OVER_SIZE_255 = "012345" + "1234567890".repeat(25);
	public static final String OVER_SIZE_65535 = "012345" + "1234567890".repeat(6553);

	// Authentication
	public static final String BEARER_TYPE = "Bearer ";

	private TestUtils() {
		/* no-op */
	}

	public static User getUser() {
		return User.builder()
			.email(USER_EMAIL)
			.nickName(USER_NICK_NAME)
			.introduce(USER_INTRODUCE)
			.prologName(USER_PROLOG_NAME)
			.provider(PROVIDER)
			.oauthId(OAUTH_ID)
			.profileImgUrl(USER_PROFILE_IMG_URL)
			.build();
	}

	public static Post getPost() {
		return Post.builder()
			.title(POST_TITLE)
			.content(POST_CONTENT)
			.openStatus(true)
			.user(USER)
			.build();
	}

	public static Comment getComment() {
		return Comment.builder()
			.content(COMMENT_CONTENT)
			.post(POST)
			.user(USER)
			.build();
	}

	public static OauthUserInfo getUserInfo() {
		return KakaoUserInfo.builder()
			.email(USER_EMAIL)
			.nickName(USER_NICK_NAME)
			.provider(PROVIDER)
			.oauthId(OAUTH_ID)
			.profileImgUrl(USER_PROFILE_IMG_URL)
			.build();
	}

	public static UserResponse getUserProfile() {
		return UserResponse.builder()
			.id(USER_ID)
			.email(USER_EMAIL)
			.nickName(USER_NICK_NAME)
			.prologName(USER_PROLOG_NAME)
			.introduce(USER_INTRODUCE)
			.profileImgUrl(USER_PROFILE_IMG_URL)
			.build();
	}

	public static RootTag getRootTag() {
		return RootTag.builder()
			.name(ROOT_TAG_NAME)
			.build();
	}

	public static PostTag getPostTag() {
		return PostTag.builder()
			.rootTag(ROOT_TAG)
			.post(POST)
			.build();
	}

	public static Series getSeries() {
		return Series.builder()
			.title(SERIES_TITLE)
			.user(USER)
			.post(POST)
			.build();
	}

	public static Like getLike() {
		return Like.builder()
			.user(USER)
			.post(POST)
			.build();
	}
}
