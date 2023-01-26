package com.prgrms.prolog.domain.series.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Series {

	private static final int TITLE_MAX_SIZE = 50;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@OneToMany(mappedBy = "series")
	private final List<Post> posts = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Series(String title, User user, Post post) {
		this.title = validateTitle(title);
		this.user = Objects.requireNonNull(user, "exception.comment.user.require");
		addPost(post);
	}

	private void addPost(Post post) {
		if (post == null) {
			return;
		}
		post.setSeries(this);
	}

	public void setUser(User user) {
		if (this.user != null) {
			this.user.getSeries().remove(this);
		}
		this.user = user;
		user.getSeries().add(this);
	}

	public void changeTitle(String title) {
		this.title = validateTitle(title);
	}

	private String validateTitle(String title) {
		checkText(title);
		checkOverLength(title, TITLE_MAX_SIZE);
		return title;
	}

	private void checkText(String text) {
		Assert.hasText(text, "exception.comment.text");
	}

	private void checkOverLength(String text, int length) {
		if (text.length() > length) {
			throw new IllegalArgumentException("exception.post.text.overLength");
		}
	}
}
