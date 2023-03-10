package com.prgrms.prolog.domain.post.model;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;
import static com.prgrms.prolog.global.util.ValidateUtil.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.dto.PostDto.UpdatePostRequest;
import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE post SET deleted = true where id=?")
@Where(clause = "deleted=false")
public class Post extends BaseEntity {

	private static final int TITLE_MAX_SIZE = 50;
	private static final int CONTENT_MAX_SIZE = 65535;

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

	@ColumnDefault("0")
	@Column(name = "like_count")
	private int likeCount;


	@Builder
	public Post(String title, String content, boolean openStatus, User user) {
		this.title = validateTitle(title);
		this.content = validateContent(content);
		this.openStatus = openStatus;
		this.user = Objects.requireNonNull(user, messageKey().exception().post().user().require().endKey());
	}

	public void setSeries(Series series) {
		if (this.series != null) {
			this.series.getPosts().remove(this);
		}
		this.series = series;
		series.getPosts().add(this);
	}

	public void addPostTags(Set<PostTag> postTags) {
		this.postTags.addAll(postTags);
	}

	public void changePost(UpdatePostRequest updateRequest) {
		validateTitle(updateRequest.title());
		validateContent(updateRequest.content());

		this.title = updateRequest.title();
		this.content = updateRequest.content();
		this.openStatus = updateRequest.openStatus();
	}


	private String validateTitle(String title) {
		checkText(title, messageKey().exception().post().title().notText().endKey());
		checkOverLength(title, TITLE_MAX_SIZE, messageKey().post().title().overLength().endKey());
		return title;
	}

	private String validateContent(String content) {
		checkText(content, messageKey().exception().post().content().notText().endKey());
		checkOverLength(content, CONTENT_MAX_SIZE, messageKey().exception().post().content().overLength().endKey());
		return content;
	}
}