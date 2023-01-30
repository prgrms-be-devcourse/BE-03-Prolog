package com.prgrms.prolog.domain.user.model;

import static com.prgrms.prolog.domain.user.dto.UserDto.*;
import static com.prgrms.prolog.global.util.ValidateUtil.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.prgrms.prolog.domain.comment.model.Comment;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.usertag.model.UserTag;
import com.prgrms.prolog.global.common.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	private static final Pattern emailPattern
		= Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Size(max = 100)
	private String email;

	@Size(max = 255)
	private String profileImgUrl;

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

	@OneToMany(mappedBy = "user")
	private final List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private final List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private final Set<UserTag> userTags = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private final List<Series> series = new ArrayList<>();

	@Builder
	public User(String email, String nickName, String introduce,
		String prologName, String provider, String oauthId, String profileImgUrl) {
		this.email = validateEmail(email);
		this.nickName = validateNickName(nickName);
		this.introduce = validateIntroduce(introduce);
		this.prologName = validatePrologName(prologName);
		this.provider = Objects.requireNonNull(provider, "exception.user.provider.null");
		this.oauthId = Objects.requireNonNull(oauthId, "exception.user.oauthId.null");
		this.profileImgUrl = profileImgUrl;
	}

	public void changeUserProfile(UpdateUserRequest updateUserRequest) {
		this.email = validateEmail(updateUserRequest.email());
		this.nickName = validateNickName(updateUserRequest.nickName());
		this.introduce = validateIntroduce(updateUserRequest.introduce());
		this.prologName = validatePrologName(updateUserRequest.prologName());
	}

	public void changeProfileImgUrl(String profileImgUrl) {
		Objects.requireNonNull(profileImgUrl, "exception.user.profile.imageUrl.null");
	}

	private String validatePrologName(String prologName) {
		checkText(prologName, "exception.user.prologName.text");
		checkOverLength(prologName, 100, "exception.user.prologName.length");
		return prologName;
	}

	private String validateIntroduce(String introduce) {
		checkOverLength(introduce, 100, "exception.user.introduce.length");
		return introduce;
	}

	private String validateNickName(String nickName) {
		checkText(nickName, "exception.user.nickName.text");
		checkOverLength(nickName, 100, "exception.user.nickName.length");
		return nickName;
	}

	private String validateEmail(String email) {
		checkText(email, "exception.user.email.text");
		checkOverLength(email, 100, "exception.user.email.length");
		checkPattern(email, emailPattern, "exception.user.email.pattern");
		return email;
	}

	private void checkPattern(String text, Pattern pattern, String message) {
		Matcher matcher = pattern.matcher(text);
		Assert.isTrue(matcher.matches(), message);
	}

	public boolean checkSameUserId(Long userId) {
		return Objects.equals(this.id, userId);
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
