package com.y2gcoder.blog.web.controller.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerIntegrationTest {
	@Autowired
	WebApplicationContext context;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestInitDB initDB;

	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	AuthService authService;

	ObjectMapper objectMapper = new ObjectMapper();
	Member member;
	Category category;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
		member = memberRepository.findByEmail(initDB.getMemberEmail1()).orElseThrow(IllegalArgumentException::new);
		category = categoryRepository.save(new Category("category"));
	}

	@Test
	@DisplayName("게시글: 생성 성공")
	void create_Normal_Success() throws Exception {
		//given
		SignInResponse signInRes = authService.signIn(new SignInRequest(member.getEmail(), initDB.getPassword()));
		ArticleCreateRequest req = new ArticleCreateRequest(
				"title",
				"content",
				"",
				category.getId(),
				null
		);
		//when
		//then
		mockMvc.perform(
						post("/api/articles")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isCreated());

		Article result = articleRepository.findAll().get(0);
		assertThat(result.getTitle()).isEqualTo("title");
		assertThat(result.getContent()).isEqualTo("content");
		assertThat(result.getThumbnailUrl()).isEqualTo("");
		assertThat(result.getMember().getId()).isEqualTo(member.getId());
	}

	@Test
	@DisplayName("게시글: 생성 실패, 토큰 없음")
	void create_NoToken_Fail() throws Exception {
		//given
		ArticleCreateRequest req = new ArticleCreateRequest(
				"title",
				"content",
				"",
				category.getId(),
				null
		);
		//when
		//then
		mockMvc.perform(
				post("/api/articles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isUnauthorized());
	}
}
