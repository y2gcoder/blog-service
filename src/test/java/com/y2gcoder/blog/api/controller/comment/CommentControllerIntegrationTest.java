package com.y2gcoder.blog.api.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.article.Article;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.article.ArticleRepository;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.comment.CommentRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.service.comment.dto.CommentUpdateRequest;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerIntegrationTest {
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
	CommentRepository commentRepository;
	@Autowired
	AuthService authService;

	ObjectMapper objectMapper = new ObjectMapper();
	Member admin;
	Member member1;
	Member member2;
	Article article;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
		admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(IllegalArgumentException::new);
		member1 = memberRepository.findByEmail(initDB.getMemberEmail1()).orElseThrow(IllegalArgumentException::new);
		member2 = memberRepository.findByEmail(initDB.getMemberEmail2()).orElseThrow(IllegalArgumentException::new);
		Category category = categoryRepository.save(new Category("category"));
		article = articleRepository.save(new Article("title", "content", "", category, admin));
	}

	@Test
	@DisplayName("댓글: 생성, 성공")
	void create_Normal_Success() throws Exception {
		//given
		SignInResponse signInRes = authService.signIn(new SignInRequest(member1.getEmail(), initDB.getPassword()));
		CommentCreateRequest req = new CommentCreateRequest(
				"댓글",
				null,
				article.getId()
		);
		//when
		//then
		mockMvc.perform(
				post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
						.header("Authorization", signInRes.getAccessToken())
		).andExpect(status().isCreated());

		List<Comment> result = commentRepository.findAll();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getContent()).isEqualTo("댓글");
		assertThat(result.get(0).getMember().getId()).isEqualTo(member1.getId());
	}

	@Test
	@DisplayName("댓글: 생성, 실패, 토큰 없음")
	void create_NoToken_Fail() throws Exception {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"댓글",
				null,
				article.getId()
		);
		//when
		//then
		mockMvc.perform(
				post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("댓글: 삭제, 성공, 자원소유자")
	void delete_Normal_Success() throws Exception {
		//given
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		SignInResponse signInRes = authService.signIn(new SignInRequest(member1.getEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/comments/{id}", comment.getId())
						.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("댓글: 삭제, 성공, 관리자")
	void delete_Admin_Success() throws Exception {
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		SignInResponse signInRes = authService.signIn(new SignInRequest(admin.getEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
						delete("/api/comments/{id}", comment.getId())
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("댓글: 삭제, 실패, 토큰 없음")
	void delete_NoToken_Fail() throws Exception {
		//given
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		//when
		//then
		mockMvc.perform(
				delete("/api/comments/{id}", comment.getId())
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("댓글: 삭제, 실패, 관리자 x 자원소유자 x")
	void delete_NotResourceOwner_Fail() throws Exception {
		//given
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		SignInResponse signInRes = authService.signIn(new SignInRequest(member2.getEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
						delete("/api/comments/{id}", comment.getId())
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("댓글: 수정, 성공, 자원소유자")
	void update_Normal_Success() throws Exception {
		//given
		Comment comment = commentRepository
				.save(
						new Comment("댓글", member1, article)
				);
		SignInResponse signInRes = authService.signIn(new SignInRequest(member1.getEmail(), initDB.getPassword()));
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		//then
		mockMvc.perform(
				patch("/api/comments/{id}", comment.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
						.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isOk());

		Comment result = commentRepository.findById(comment.getId()).get();
		assertThat(result.getContent()).isEqualTo(req.getContent());
	}

	@Test
	@DisplayName("댓글: 수정, 성공, 관리자")
	void update_Admin_Success() throws Exception {
		//given
		Comment comment = commentRepository
				.save(
						new Comment("댓글", member1, article)
				);
		SignInResponse signInRes = authService.signIn(new SignInRequest(admin.getEmail(), initDB.getPassword()));
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		//then
		mockMvc.perform(
						patch("/api/comments/{id}", comment.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isOk());
		Comment result = commentRepository.findById(comment.getId()).get();
		assertThat(result.getContent()).isEqualTo(req.getContent());
	}

	@Test
	@DisplayName("댓글: 수정, 실패, 토큰 없음")
	void update_NoToken_Fail() throws Exception {
		//given
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		//then
		mockMvc.perform(
				patch("/api/comments/{id}", comment.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("댓글: 수정, 실패, 관리자 x, 자원소유자 x")
	void update_NotResourceOwner_Fail() throws Exception {
		//given
		Comment comment = commentRepository.save(new Comment("댓글", member1, article));
		SignInResponse signInRes = authService.signIn(new SignInRequest(member2.getEmail(), initDB.getPassword()));
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		//then
		mockMvc.perform(
						patch("/api/comments/{id}", comment.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
								.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("댓글: 목록 조회, 성공")
	void readAll_Normal_Success() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
			get("/api/comments")
					.param("size", "10")
					.param("articleId", String.valueOf(article.getId()))
		).andExpect(status().isOk());
	}
}
