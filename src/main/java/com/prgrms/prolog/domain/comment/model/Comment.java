package com.prgrms.prolog.domain.comment.model;

import static javax.persistence.FetchType.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Comment extends BaseEntity {

	private static final String CONTENT_OVER_LENGTH_MESSAGE = "댓글 내용의 최대 글자 수를 초과하였습나다.";
	private static final String CONTENT_EMPTY_VALUE_MESSAGE = "댓글 내용은 빈 값일 수 없습니다.";
	private static final int CONTENT_MAX_SIZE = 255;

	@Id
	@GeneratedValue
	private Long id;

	@Size(max = 255)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	public Comment(String content, Post post) {
		this.content = validateContent(content);
		this.post = Objects.requireNonNull(post, "해당 하는 게시글이 필요합니다");
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
}

