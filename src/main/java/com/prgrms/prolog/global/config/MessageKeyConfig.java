package com.prgrms.prolog.global.config;

public class MessageKeyConfig {

	private MessageKeyConfig() {
	}

	private static final String EXCEPTION = "exception";
	private static final String USER = ".user";
	private static final String POST = ".post";
	private static final String COMMENT = ".comment";
	private static final String POST_TAG = ".postTag";
	private static final String ROOT_TAG = ".rootTag";
	private static final String USER_TAG = ".userTag";
	private static final String LIKE = ".like";
	private static final String FILE = ".file";
	private static final String JWT_AUTHENTICATION = ".jwtAuthentication";
	private static final String EMAIL = ".email";
	private static final String TITLE = ".title";
	private static final String NAME = ".name";
	private static final String ID = ".id";
	private static final String CONTENT = ".content";
	private static final String TEXT = ".text";
	private static final String PROVIDER = ".provider";
	private static final String OAUTH_ID = ".oauthId";
	private static final String PROFILE = ".profile";
	private static final String IMAGE_URL = ".imageUrl";
	private static final String PROLOG_NAME = ".prologName";
	private static final String INTRODUCE = ".introduce";
	private static final String NICKNAME = ".nickName";
	private static final String COUNT = ".count";
	private static final String POSITIVE_OR_ZERO = ".positiveOrZero";
	private static final String POSITIVE = ".positive";
	private static final String PATTERN = ".pattern";
	private static final String ACCESS = ".access";
	private static final String NULL = ".null";
	private static final String EMPTY = ".empty";
	private static final String REQUIRE = ".require";
	private static final String CONVERT = ".convert";
	private static final String IO = ".io";
	private static final String NOT_TEXT = ".notText";
	private static final String NOT_EXISTS = ".notExists";
	private static final String NOT_ACCESS = ".notAccess";
	private static final String ALREADY_EXISTS = ".alreadyExists";
	private static final String OVER_LENGTH = ".overLength";
	private static final String NOT_OWNER = ".notOwner";
	private static final String NOT_SAME = ".notSame";
	private static final String TOKEN = ".token";
	private static final String IS_AUTHENTICATED = ".isAuthenticated";

	private String key;

	public static MessageKeyConfig messageKey() {
		return new MessageKeyConfig();
	}

	public MessageKeyConfig exception() {
		this.key += EXCEPTION;
		return this;
	}

	public MessageKeyConfig user() {
		this.key += USER;
		return this;
	}

	public MessageKeyConfig post() {
		this.key += POST;
		return this;
	}

	public MessageKeyConfig postTag() {
		this.key += POST_TAG;
		return this;
	}

	public MessageKeyConfig rootTag() {
		this.key += ROOT_TAG;
		return this;
	}

	public MessageKeyConfig userTag() {
		this.key += USER_TAG;
		return this;
	}

	public MessageKeyConfig comment() {
		this.key += COMMENT;
		return this;
	}

	public MessageKeyConfig like() {
		this.key += LIKE;
		return this;
	}

	public MessageKeyConfig file() {
		this.key += FILE;
		return this;
	}

	public MessageKeyConfig jwtAuthentication() {
		this.key += JWT_AUTHENTICATION;
		return this;
	}

	public MessageKeyConfig email() {
		this.key += EMAIL;
		return this;
	}

	public MessageKeyConfig title() {
		this.key += TITLE;
		return this;
	}

	public MessageKeyConfig name() {
		this.key += NAME;
		return this;
	}

	public MessageKeyConfig id() {
		this.key += ID;
		return this;
	}

	public MessageKeyConfig content() {
		this.key += CONTENT;
		return this;
	}

	public MessageKeyConfig text() {
		this.key += TEXT;
		return this;
	}

	public MessageKeyConfig provider() {
		this.key += PROVIDER;
		return this;
	}

	public MessageKeyConfig oauthId() {
		this.key += OAUTH_ID;
		return this;
	}

	public MessageKeyConfig profile() {
		this.key += PROFILE;
		return this;
	}

	public MessageKeyConfig imageUrl() {
		this.key += IMAGE_URL;
		return this;
	}

	public MessageKeyConfig prologName() {
		this.key += PROLOG_NAME;
		return this;
	}

	public MessageKeyConfig introduce() {
		this.key += INTRODUCE;
		return this;
	}

	public MessageKeyConfig nickName() {
		this.key += NICKNAME;
		return this;
	}

	public MessageKeyConfig count() {
		this.key += COUNT;
		return this;
	}

	public MessageKeyConfig positiveOrZero() {
		this.key += POSITIVE_OR_ZERO;
		return this;
	}

	public MessageKeyConfig positive() {
		this.key += POSITIVE;
		return this;
	}

	public MessageKeyConfig pattern() {
		this.key += PATTERN;
		return this;
	}

	public MessageKeyConfig access() {
		this.key += ACCESS;
		return this;
	}

	public MessageKeyConfig isNull() {
		this.key += NULL;
		return this;
	}

	public MessageKeyConfig empty() {
		this.key += EMPTY;
		return this;
	}

	public MessageKeyConfig require() {
		this.key += REQUIRE;
		return this;
	}

	public MessageKeyConfig convert() {
		this.key += CONVERT;
		return this;
	}

	public MessageKeyConfig io() {
		this.key += IO;
		return this;
	}

	public MessageKeyConfig notText() {
		this.key += NOT_TEXT;
		return this;
	}

	public MessageKeyConfig notExists() {
		this.key += NOT_EXISTS;
		return this;
	}

	public MessageKeyConfig notAccess() {
		this.key += NOT_ACCESS;
		return this;
	}

	public MessageKeyConfig alreadyExists() {
		this.key += ALREADY_EXISTS;
		return this;
	}

	public MessageKeyConfig overLength() {
		this.key += OVER_LENGTH;
		return this;
	}

	public MessageKeyConfig notOwner() {
		this.key += NOT_OWNER;
		return this;
	}

	public MessageKeyConfig notSame() {
		this.key += NOT_SAME;
		return this;
	}

	public MessageKeyConfig token() {
		this.key += TOKEN;
		return this;
	}

	public MessageKeyConfig isAuthenticated() {
		this.key += IS_AUTHENTICATED;
		return this;
	}

	public String endKey() {
		return this.key;
	}
}
