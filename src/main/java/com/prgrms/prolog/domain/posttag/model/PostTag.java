package com.prgrms.prolog.domain.posttag.model;

import static com.prgrms.prolog.global.config.MessageKeyConfig.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.roottag.model.RootTag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostTag {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "root_tag_id")
	private RootTag rootTag;

	@Builder
	public PostTag(Post post, RootTag rootTag) {
		this.post = validatePost(post);
		this.rootTag = validateRootTag(rootTag);
	}

	private RootTag validateRootTag(RootTag rootTag) {
		Assert.notNull(rootTag, messageKey().exception().postTag().rootTag().isNull().endKey());
		return rootTag;
	}

	private Post validatePost(Post post) {
		Assert.notNull(post, messageKey().exception().postTag().post().isNull().endKey());
		return post;
	}
}
