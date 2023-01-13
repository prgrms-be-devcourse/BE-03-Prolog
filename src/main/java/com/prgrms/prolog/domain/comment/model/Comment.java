package com.prgrms.prolog.domain.comment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(name = "comment_id")
	private Long id;

	@Size(max = 255)
	private String content;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
}
