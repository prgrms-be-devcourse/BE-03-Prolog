package com.prgrms.prolog.domain.post.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.comment.model.Comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Post {

	private static final int TITLE_MAX_SIZE = 50;

	@Id
	@GeneratedValue
	private Long id;

	private String title;

	@Lob
	private String content;

	private boolean openStatus;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();


	@Builder
	public Post(String title, String content, boolean openStatus) {
		validateText(title);
		checkOverLength(title);
		validateText(content);

		this.title = title;
		this.content = content;
		this.openStatus = openStatus;
	}

	private void validateText(String text) {
		Assert.hasText(text, "빈 값일 수 없는 데이터입니다.");
	}

	private void checkOverLength(String title) {
		if(title.length() > TITLE_MAX_SIZE) {
			log.debug("최대 글자수 {} 를 초과하였습니다.", TITLE_MAX_SIZE);
			throw new IllegalArgumentException("게시물의 제목 입력값이 올바르지 않습니다.");
		}
	}
}
