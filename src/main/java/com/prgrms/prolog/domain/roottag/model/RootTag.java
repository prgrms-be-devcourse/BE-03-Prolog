package com.prgrms.prolog.domain.roottag.model;

import static javax.persistence.GenerationType.*;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.usertag.model.UserTag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RootTag {

	private static final int NAME_MAX_LENGTH = 100;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@OneToMany(mappedBy = "rootTag")
	private final Set<PostTag> postTags = new HashSet<>();

	@OneToMany(mappedBy = "rootTag")
	private final Set<UserTag> userTag = new HashSet<>();

	@Builder
	public RootTag(String name) {
		this.name = validateRootTagName(name);
	}

	private String validateRootTagName(String name) {
		Assert.hasText(name, "exception.rootTag.name.text");
		Assert.isTrue(name.length() <= NAME_MAX_LENGTH, "exception.rootTag.name.length");
		return name;
	}
}
