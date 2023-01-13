package com.prgrms.prolog.user.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = false, length = 100)
	private String nickName;

	@Column(nullable = true, length = 100)
	private String introduce;

	@Column(nullable = false, length = 100)
	private String prologName;

	@Column(nullable = false, length = 100)
	private String provider;

	@Column(nullable = false, length = 100)
	private String oauthId;

	@Builder
	public User(String email, String nickName, String introduce,
		String prologName, String provider, String oauthId) {

		validateEmail(email);
		validateNickName(nickName);
		validateIntroduce(introduce);
		validatePrologName(prologName);

		this.email = email;
		this.nickName = nickName;
		this.introduce = introduce;
		this.prologName = prologName;
		this.provider = provider;
		this.oauthId = oauthId;
	}

	private void validatePrologName(String prologName) {
		Assert.hasText(prologName, "블로그 제목은 빈 값일 수 없습니다.");
		checkOverLength(prologName, 100, "블로그 제목은 최대 100글자 이내로 작성해야 합니다.");
	}

	private void validateIntroduce(String introduce) {
		checkOverLength(introduce, 100, "한줄 소개는 최대 100글자 이내로 작성해야 합니다.");
	}

	private void validateNickName(String nickName) {
		Assert.hasText(nickName, "닉네임은 빈 값일 수 없습니다.");
		checkOverLength(nickName, 100, "닉네임은 최대 100글자 이내로 작성해야 합니다.");
	}

	private void validateEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		Assert.hasText(email, "이메일은 빈 값일 수 없습니다.");
		Assert.isTrue(matcher.matches(), "이메일 형식이 올바르지 않습니다.");
		checkOverLength(email, 100, "이메일은 최대 100글자 이내로 작성해야 합니다..");
	}

	private void checkOverLength(String text, int length, String message) {
		if (text.length() > length) {
			log.debug("글자 수 초과");
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", email='" + email +
			", nickName='" + nickName +
			", introduce='" + introduce +
			", prologName='" + prologName +
			", provider='" + provider +
			", oauthId='" + oauthId +
			'}';
	}
}
