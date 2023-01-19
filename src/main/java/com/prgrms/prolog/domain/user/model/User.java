package com.prgrms.prolog.domain.user.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	public static final String EMAIL_FIELD = "이메일";
	public static final String NICK_NAME_FIELD = "닉네임";
	public static final String PROLOG_NAME_FIELD = "블로그 제목";
	public static final String INTRODUCE_FIELD = "한줄 소개";

	public static final String OVER_LENGTH_MESSAGE = "는 최대 글자 수를 초과하였습나다.";
	public static final String EMPTY_VALUE_MESSAGE = "는 빈 값일 수 없습니다.";
	public static final String NULL_VALUE_MESSAGE = "는 NULL일 수 없습니다.";
	public static final String WRONG_EMAIL_PATTERN_MESSAGE = "이메일 형식이 올바르지 않습니다.";

	private static final Pattern emailPattern
		= Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
	@OneToMany(mappedBy = "user")
	private final List<Post> posts = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 100)
	private String email;
	@Size(max = 100)
	private String nickName;
	@Size(max = 100)
	private String introduce;
	@Size(max = 100)
	private String prologName;
	@Size(max = 100)
	private String provider;
	@Size(max = 100)
	private String oauthId;

	@Builder
	public User(String email, String nickName, String introduce,
		String prologName, String provider, String oauthId) {
		this.email = validateEmail(email);
		this.nickName = validateNickName(nickName);
		this.introduce = validateIntroduce(introduce);
		this.prologName = validatePrologName(prologName);
		this.provider = Objects.requireNonNull(provider, "provider" + NULL_VALUE_MESSAGE);
		this.oauthId = Objects.requireNonNull(oauthId, "oauthId" + NULL_VALUE_MESSAGE);
	}

	private String validatePrologName(String prologName) {
		checkText(prologName, PROLOG_NAME_FIELD);
		checkOverLength(prologName, 100, PROLOG_NAME_FIELD);
		return prologName;
	}

	private String validateIntroduce(String introduce) {
		checkOverLength(introduce, 100, PROLOG_NAME_FIELD);
		return introduce;
	}

	private String validateNickName(String nickName) {
		checkText(nickName, NICK_NAME_FIELD);
		checkOverLength(nickName, 100, NICK_NAME_FIELD);
		return nickName;
	}

	private String validateEmail(String email) {
		checkText(email, EMAIL_FIELD);
		checkOverLength(email, 100, EMAIL_FIELD);
		checkPattern(email, emailPattern, WRONG_EMAIL_PATTERN_MESSAGE);
		return email;
	}

	private void checkText(String text, String fieldName) {
		Assert.hasText(text, fieldName + EMPTY_VALUE_MESSAGE);
	}

	private void checkOverLength(String text, int length, String fieldName) {
		if (Objects.nonNull(text) && text.length() > length) {
			throw new IllegalArgumentException(fieldName + OVER_LENGTH_MESSAGE);
		}
	}

	private void checkPattern(String text, Pattern pattern, String message) {
		Matcher matcher = pattern.matcher(text);
		Assert.isTrue(matcher.matches(), message);
	}

	public void changeEmail(String email) {
		this.email = validateEmail(email);
	}

	public void changeNickName(String nickName) {
		this.nickName = validateNickName(nickName);
	}

	public void changeIntroduce(String introduce) {
		this.introduce = validateIntroduce(introduce);
	}

	public void changePrologName(String prologName) {
		this.prologName = validatePrologName(prologName);
	}

	@Override
	public String toString() {
		return "User{"
			+ "id=" + id
			+ ", email='" + email
			+ ", nickName='" + nickName
			+ ", introduce='" + introduce
			+ ", prologName='" + prologName
			+ ", provider='" + provider
			+ ", oauthId='" + oauthId
			+ '}';
	}
}
