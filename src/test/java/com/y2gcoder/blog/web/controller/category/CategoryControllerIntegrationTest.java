package com.y2gcoder.blog.web.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.entity.category.Category;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.category.CategoryRepository;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.category.dto.CategoryCreateRequest;
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
public class CategoryControllerIntegrationTest {
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
	AuthService authService;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
	}

	@Test
	@DisplayName("카테고리: 목록 조회 성공")
	void readAll_Normal_Success() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
				get("/api/categories")
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("카테고리: 생성 성공")
	void create_Normal_Success() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		SignInResponse adminSignInRes = authService
				.signIn(new SignInRequest(initDB.getAdminEmail(), initDB.getPassword()));
		//when
		mockMvc.perform(
				post("/api/categories")
						.header("Authorization", adminSignInRes.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isCreated());
		//then
		List<Category> result = categoryRepository.findAll();
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("카테고리: 생성 실패, 중복 카테고리명")
	void create_DuplicateName_Fail() throws Exception {
		//given
		categoryRepository.save(new Category("category"));
		CategoryCreateRequest req = createCategoryCreateRequest();
		SignInResponse adminSignInRes = authService
				.signIn(new SignInRequest(initDB.getAdminEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
						post("/api/categories")
								.header("Authorization", adminSignInRes.getAccessToken())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("카테고리: 생성 실패, 토큰 없음")
	void create_NoToken_Fail() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		//when
		//then
		mockMvc.perform(
				post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("카테고리: 생성 실패, 일반 사용자")
	void create_NormalMember_Fail() throws Exception {
		//given
		CategoryCreateRequest req = createCategoryCreateRequest();
		SignInResponse signInRes = authService
				.signIn(new SignInRequest(initDB.getMemberEmail1(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				post("/api/categories")
						.header("Authorization", signInRes.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("카테고리: 삭제 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Category category = categoryRepository.save(new Category("category"));
		SignInResponse adminSignInRes = authService
				.signIn(new SignInRequest(initDB.getAdminEmail(), initDB.getPassword()));
		//when
		mockMvc.perform(
				delete("/api/categories/{id}", category.getId())
						.header("Authorization", adminSignInRes.getAccessToken())
		).andExpect(status().isOk());
		//then
		List<Category> result = categoryRepository.findAll();
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("카테고리: 삭제 실패, 토큰 없음")
	void delete_NoToken_Fail() throws Exception {
		//given
		Category category = categoryRepository.save(new Category("category"));
		//when
		//then
		mockMvc.perform(
				delete("/api/categories/{id}", category.getId())
		).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("카테고리: 삭제 실패, 일반 사용자")
	void delete_NormalMember_Fail() throws Exception {
		//given
		Category category = categoryRepository.save(new Category("category"));
		SignInResponse signInRes = authService
				.signIn(new SignInRequest(initDB.getMemberEmail1(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/categories/{id}", category.getId())
						.header("Authorization", signInRes.getAccessToken())
				)
				.andExpect(status().isForbidden());
	}

	private static CategoryCreateRequest createCategoryCreateRequest() {
		return new CategoryCreateRequest("category");
	}

}
