package com.prgrms.prolog.domain.usertag.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.roottag.model.RootTag;
import com.prgrms.prolog.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserTag {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@PositiveOrZero
	private Integer count;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "root_tag_id")
	private RootTag rootTag;

	public UserTag(User user, RootTag rootTag) {
		this.user = validateUser(user);
		this.rootTag = validateRootTag(rootTag);
	}

	@Builder
	public UserTag(Integer count, User user, RootTag rootTag) {
		this.user = validateUser(user);
		this.rootTag = validateRootTag(rootTag);
		this.count = validateCount(count);
	}

	public void increaseCount(int count) {
		Assert.isTrue(count >= 0, "exception.userTag.count.positive");
		this.count += count;
	}

	public void decreaseCount(int count) {
		Assert.isTrue(count >= 0, "exception.userTag.count.positive");
		this.count -= count;
	}

	private RootTag validateRootTag(RootTag rootTag) {
		Assert.notNull(rootTag, "exception.userTag.rootTag.null");
		return rootTag;
	}

	private User validateUser(User user) {
		Assert.notNull(user, "exception.userTag.user.null");
		return user;
	}

	private Integer validateCount(Integer count) {
		Assert.isTrue(count >= 0, "exception.userTag.count.positiveOrZero");
		return count;
	}

	public boolean isCountZero() {
		return this.count == 0;
	}
}
