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
import com.y2gcoder.blog.service.article.dto.ArticleUpdateRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	Member admin;
	Member member1;
	Member member2;
	Category category;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
		admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(IllegalArgumentException::new);
		member1 = memberRepository.findByEmail(initDB.getMemberEmail1()).orElseThrow(IllegalArgumentException::new);
		member2 = memberRepository.findByEmail(initDB.getMemberEmail2()).orElseThrow(IllegalArgumentException::new);
		category = categoryRepository.save(new Category("category"));
	}

	@Test
	@DisplayName("게시글: 생성 성공")
	void create_Normal_Success() throws Exception {
		//given
		SignInResponse signInRes = authService.signIn(new SignInRequest(admin.getEmail(), initDB.getPassword()));
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
		assertThat(result.getMember().getId()).isEqualTo(admin.getId());
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

	@Test
	@DisplayName("게시글: 단건 조회, 성공")
	void read_Normal_Success() throws Exception {
		//given
		Article article = articleRepository.save(
				new Article("title", "content", "", category, admin)
		);
		//when
		//then
		mockMvc.perform(
				get("/api/articles/{id}", article.getId())
				)
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("게시글: 삭제, 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", null, category, admin));
		SignInResponse signInRes = authService.signIn(new SignInRequest(admin.getEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/articles/{id}", article.getId())
						.header("Authorization", signInRes.getAccessToken())
		).andExpect(status().isOk());

		assertThatThrownBy(() -> articleRepository.findById(article.getId()).orElseThrow(IllegalArgumentException::new))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("게시글: 삭제, 실패, 토큰 없음")
	void delete_NoToken_Fail() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", null, category, admin));
		//when
		//then
		mockMvc.perform(
				delete("/api/articles/{id}", article.getId())
		).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("게시글: 삭제, 실패, 일반 사용자")
	void delete_NormalMember_Fail() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", null, category, admin));
		SignInResponse signInRes = authService.signIn(new SignInRequest(member1.getEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
			delete("/api/articles/{id}", article.getId())
					.header("Authorization", signInRes.getAccessToken())
		).andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("게시글: 수정, 성공")
	void update_Normal_Success() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", "", category, admin));
		SignInResponse signInRes = authService.signIn(new SignInRequest(admin.getEmail(), initDB.getPassword()));
		ArticleUpdateRequest req = new ArticleUpdateRequest(
				"updatedTitle",
				"updatedContent",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80"
		);
		//when
		//then
		mockMvc.perform(
				patch("/api/articles/{id}", article.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
						.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isOk());
		Article updatedArticle = articleRepository.findById(article.getId()).orElseThrow(IllegalArgumentException::new);
		assertThat(updatedArticle.getTitle()).isEqualTo(req.getTitle());
		assertThat(updatedArticle.getContent()).isEqualTo(req.getContent());
		assertThat(updatedArticle.getThumbnailUrl()).isEqualTo(req.getThumbnailUrl());
	}

	@Test
	@DisplayName("게시글: 수정, 실패, 토큰 없음")
	void update_NoToken_Fail() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", "", category, admin));
		ArticleUpdateRequest req = new ArticleUpdateRequest(
				"updatedTitle",
				"updatedContent",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80"
		);
		//when
		//then
		mockMvc.perform(
						patch("/api/articles/{id}", article.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("게시글: 수정, 실패, 일반 사용자")
	void update_NormalMember_Fail() throws Exception {
		//given
		Article article = articleRepository
				.save(new Article("title", "content", "", category, admin));
		SignInResponse signInRes = authService.signIn(new SignInRequest(member1.getEmail(), initDB.getPassword()));
		ArticleUpdateRequest req = new ArticleUpdateRequest(
				"updatedTitle",
				"updatedContent",
				"https://images.unsplash.com/photo-1461887046916-c7426e65460d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80"
		);
		//when
		//then
		mockMvc.perform(
						patch("/api/articles/{id}", article.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isForbidden());
	}
}
