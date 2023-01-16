package com.prgrms.prolog.domain.post.model;

import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	private static final int TITLE_MAX_SIZE = 50;
	private static final int CONTENT_MAX_SIZE = 65535;

	@OneToMany(mappedBy = "post")
	private final List<Comment> comments = new ArrayList<>();

	@Id
	@GeneratedValue
	private Long id;

	@Size(max = TITLE_MAX_SIZE)
	private String title;

	@Lob
	private String content;

	private boolean openStatus;

	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Post(String title, String content, boolean openStatus, User user) {
		this.title = validateTitle(title);
		this.content = validateContent(content);
		this.openStatus = openStatus;
		this.user = Objects.requireNonNull(user, "게시글은 작성자 정보가 필요합니다.");
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
		Assert.hasText(text, "빈 값일 수 없는 데이터입니다.");
	}

	private void checkOverLength(String text, int length) {
		if (text.length() > length) {
			throw new IllegalArgumentException("입력할 수 있는 범위를 초과하였습니다.");
		}
	}
}
