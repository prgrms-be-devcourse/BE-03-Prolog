package com.prgrms.prolog.base;

import static com.prgrms.prolog.global.jwt.JwtTokenProvider.*;
import static com.prgrms.prolog.utils.TestUtils.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.prolog.config.RestDocsConfig;
import com.prgrms.prolog.config.TestContainerConfig;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.global.jwt.JwtTokenProvider;

@Transactional
@ExtendWith(RestDocumentationExtension.class)
@Import({RestDocsConfig.class, TestContainerConfig.class})
@SpringBootTest
public abstract class ControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected JwtTokenProvider jwtTokenProvider;

	@Autowired
	protected UserRepository userRepository;

	protected MockMvc mockMvc;
	protected User savedUser;
	protected Long savedUserId;
	protected String ACCESS_TOKEN;

	@BeforeEach
	void setUpRestDocs(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(restDocumentation))
			.apply(springSecurity())
			.alwaysDo(restDocs)
			.build();
	}

	@BeforeEach
	void setUpLogin() {
		savedUser = userRepository.save(USER);
		Claims claims = Claims.from(savedUser.getId(), "ROLE_USER");
		ACCESS_TOKEN = jwtTokenProvider.createAccessToken(claims);
		savedUserId = savedUser.getId();
	}

}
