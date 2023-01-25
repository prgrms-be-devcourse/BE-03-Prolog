package com.prgrms.prolog.domain.comment.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

	private static final String CONTENT_OVER_LENGTH_MESSAGE = "exception.comment.content.overLength";
	private static final String CONTENT_EMPTY_VALUE_MESSAGE = "exception.comment.content.empty";
	private static final int CONTENT_MAX_SIZE = 255;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Size(max = 255)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Comment(String content, Post post, User user) {
		this.content = validateContent(content);
		this.post = Objects.requireNonNull(post, "exception.post.require");
		this.user = Objects.requireNonNull(user, "exception.user.require");
	}

	public void changeContent(String content) {
		this.content = validateContent(content);
	}

	private String validateContent(String content) {
		Assert.hasText(content, CONTENT_OVER_LENGTH_MESSAGE);
		Assert.isTrue(content.length() <= CONTENT_MAX_SIZE, CONTENT_OVER_LENGTH_MESSAGE);
		return content;
	}

	public void setPost(Post post) {
		if (this.post != null) {
			this.post.getComments().remove(this);
		}
		this.post = post;
		post.getComments().add(this);
	}

	public boolean checkUserEmail(String email) {
		Assert.notNull(user, "exception.user.notExists");
		return this.user.checkSameEmail(email);
	}
}

