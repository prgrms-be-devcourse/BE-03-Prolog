package com.prgrms.prolog.domain.series.model;

import static com.prgrms.prolog.domain.series.dto.SeriesDto.*;
import static com.prgrms.prolog.global.config.MessageKeyConfig.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE series SET deleted = true where id=?")
@Where(clause = "deleted=false")
public class Series extends BaseEntity {

	private static final int TITLE_MAX_SIZE = 50;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String title;

	private boolean deleted;

	@OneToMany(mappedBy = "series")
	private final List<Post> posts = new ArrayList<>();

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Series(String title, User user, Post post) {
		this.title = validateTitle(title);
		this.user = Objects.requireNonNull(user, messageKey().exception().comment().user().require().endKey());
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

	public Series changeSeriesTitle(UpdateSeriesRequest updateSeriesRequest) {
		this.title = validateTitle(updateSeriesRequest.title());
		return this;
	}

	private String validateTitle(String title) {
		checkText(title);
		checkOverLength(title, TITLE_MAX_SIZE);
		return title;
	}

	private void checkText(String text) {
		Assert.hasText(text, messageKey().exception().comment().text().endKey());
	}

	private void checkOverLength(String text, int length) {
		if (text.length() > length) {
			throw new IllegalArgumentException(messageKey().exception().post().text().overLength().endKey());
		}
	}
}
