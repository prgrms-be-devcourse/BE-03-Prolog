package com.prgrms.prolog.domain.post.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

	private static final int TITLE_MAX_SIZE = 50;
	private static final int CONTENT_MAX_SIZE = 65535;

	private static final String USER_INFO_NEED_MESSAGE = "게시글은 작성자 정보가 필요합니다.";
	private static final String NOT_NULL_DATA_MESSAGE = "빈 값일 수 없는 데이터입니다.";
	private static final String OVER_LENGTH_MESSAGE = "입력할 수 있는 범위를 초과하였습니다.";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String title;

	@Lob
	private String content;

	private boolean openStatus = true;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post")
	private final List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "post")
	private final Set<PostTag> postTags = new HashSet<>();

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "series_id")
	private Series series;

	@Builder
	public Post(String title, String content, boolean openStatus, User user) {
		this.title = validateTitle(title);
		this.content = validateContent(content);
		this.openStatus = openStatus;
		this.user = Objects.requireNonNull(user, "exception.comment.user.require");
	}

	public void setUser(User user) {
		if (this.user != null) {
			this.user.getPosts().remove(this);
		}
		this.user = user;
		user.getPosts().add(this);
	}

	public void changeTitle(String title) {
		this.title = validateTitle(title);
	}

	public void changeContent(String content) {
		this.content = validateContent(content);
	}

	public void changeOpenStatus(boolean openStatus) {
		this.openStatus = openStatus;
	}

	public void changePost(UpdateRequest updateRequest) {
		validateTitle(updateRequest.title());
		validateContent(updateRequest.content());

		this.title = updateRequest.title();
		this.content = updateRequest.content();
		this.openStatus = updateRequest.openStatus();
	}

	public void addPostTagsFrom(Set<PostTag> postTags) {
		this.postTags.addAll(postTags);
	}

	private String validateTitle(String title) {
		checkText(title);
		checkOverLength(title, TITLE_MAX_SIZE);
		return title;
	}

	private String validateContent(String content) {
		checkText(content);
		checkOverLength(content, CONTENT_MAX_SIZE);
		return content;
	}

	private void checkText(String text) {
		Assert.hasText(text, "exception.comment.text");
	}

	private void checkOverLength(String text, int length) {
		if (text.length() > length) {
			throw new IllegalArgumentException("exception.post.text.overLength");
		}
	}

	public void setSeries(Series series) {
		if (this.series != null) {
			this.series.getPosts().remove(this);
		}
		this.series = series;
		series.getPosts().add(this);
	}
}