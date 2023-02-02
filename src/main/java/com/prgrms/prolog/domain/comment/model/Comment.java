package com.prgrms.prolog.domain.comment.model;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;
import static com.prgrms.prolog.global.util.ValidateUtil.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE comment SET deleted = true where id=?")
@Where(clause = "deleted=false")
public class Comment extends BaseEntity {

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
		this.post = Objects.requireNonNull(post, messageKey().exception().post().require().endKey());
		this.user = Objects.requireNonNull(user, messageKey().exception().user().require().endKey());
	}

	public void setPost(Post post) {
		if (this.post != null) {
			this.post.getComments().remove(this);
		}
		this.post = post;
		post.getComments().add(this);
	}

	public void changeContent(String content) {
		this.content = validateContent(content);
	}

	private String validateContent(String content) {
		checkOverLength(content, CONTENT_MAX_SIZE, messageKey().exception().comment().content().overLength().endKey());
		checkText(content, messageKey().exception().comment().content().text().endKey());
		return content;
	}
}

