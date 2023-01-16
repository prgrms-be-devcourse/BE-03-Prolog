package com.prgrms.prolog.domain.comment.model;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.prgrms.prolog.domain.post.model.Post;

import lombok.Getter;

@Entity
@Getter
public class Comment {

	@Id
	@GeneratedValue
	private Long id;

	@Size(max = 255)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
}
