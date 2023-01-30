package com.prgrms.prolog.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

	private static final String email = "test@test.com";
	private static final String nickName = "테스터";
	private static final String introduce = "테스트하는 중";
	private static final String prologName = "테스터의 prolog";
	private static final String provider = "kakao";
	private static final String oauthId = "1";
	private static final String SIZE_100_STRING_DUMMY = "0" + "1234567890".repeat(10);

	private static Stream<Arguments> provideStringDummy() {
		return Stream.of(
			Arguments.of("  ", true),
			Arguments.of(SIZE_100_STRING_DUMMY, true)
		);
	}

	@Test
	@DisplayName("user 생성")
	void createTest() {
		//given & when
		User user = User.builder()
			.email(email)
			.nickName(nickName)
			.introduce(introduce)
			.prologName(prologName)
			.provider(provider)
			.oauthId(oauthId)
			.build();
		//then
		assertThat(user)
			.hasFieldOrPropertyWithValue("email", email)
			.hasFieldOrPropertyWithValue("nickName", nickName)
			.hasFieldOrPropertyWithValue("introduce", introduce)
			.hasFieldOrPropertyWithValue("prologName", prologName)
			.hasFieldOrPropertyWithValue("provider", provider)
			.hasFieldOrPropertyWithValue("oauthId", oauthId);
	}

	@ParameterizedTest
	@DisplayName("email 유효성 검증")
	@NullAndEmptySource
	@ValueSource(strings = {" ", "이메일", "이메일@"})
	void emailValidateTest(String inputEmail) {
		//given & when & then
		assertThatThrownBy(() -> getBuilder().email(inputEmail).build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이메일");
	}

	@ParameterizedTest
	@DisplayName("nickName 유효성 검증")
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	void nickNameValidateTest(String inputNickName) {
		//given & when & then
		assertThatThrownBy(
			() -> getBuilder().nickName(inputNickName)
				.email(email).build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("닉네임");
	}

	@ParameterizedTest
	@DisplayName("introduce 유효성 검증")
	@NullAndEmptySource
	void introduceValidateTest(String inputIntroduce) {
		//given & when & then
		User user = User.builder()
			.email(email)
			.nickName(nickName)
			.introduce(inputIntroduce)
			.prologName(prologName)
			.provider(provider)
			.oauthId(oauthId)
			.build();

		assertThat(user).isNotNull();
	}

	@ParameterizedTest
	@DisplayName("prologName 유효성 검증")
	@NullAndEmptySource
	@MethodSource("provideStringDummy")
	void prologNameValidateTest(String inputPrologName) {
		//given & when & then
		assertThatThrownBy(
			() -> getBuilder().prologName(inputPrologName)
				.email(email).nickName(nickName)
				.build())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("블로그 제목");
	}

	@ParameterizedTest
	@DisplayName("oauthId 유효성 검증")
	@NullSource
	void providerValidateTest(String input) {
		//given & when & then
		assertThatThrownBy(
			() -> getBuilder().provider(input)
				.email(email).nickName(nickName).prologName(prologName)
				.build())
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("provider는 NULL");
	}

	@ParameterizedTest
	@DisplayName("oauthId 유효성 검증")
	@NullSource
	void oauthIdValidateTest(String input) {
		//given & when & then
		assertThatThrownBy(
			() -> getBuilder().oauthId(input)
				.email(email).nickName(nickName).prologName(prologName).provider(provider)
				.build())
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("oauthId는 NULL");
	}

	private User.UserBuilder getBuilder() {
		return User.builder();
	}

}