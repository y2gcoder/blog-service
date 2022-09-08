package com.y2gcoder.blog.web.controller.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.article.ArticleService;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.article.dto.ArticleUpdateRequest;
import com.y2gcoder.blog.web.advice.ExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerAdviceTest {
	@InjectMocks ArticleController articleController;
	@Mock
	ArticleService articleService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(articleController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	@DisplayName("게시글: 생성 실패, 회원 없음")
	void create_NotFoundMember_Fail() throws Exception {
		//given
		given(articleService.create(any())).willThrow(IllegalArgumentException.class);
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				null,
				1L,
				null
		);
		//when
		//then
		mockMvc.perform(
						post("/api/articles")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시글: 생성 실패, 카테고리 없음")
	void create_NotFoundCategory_Fail() throws Exception {
		//given
		given(articleService.create(any())).willThrow(IllegalArgumentException.class);
		ArticleCreateRequest req = new ArticleCreateRequest(
				"my title",
				"my content",
				null,
				1L,
				null
		);
		//when
		//then
		mockMvc.perform(
						post("/api/articles")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시글: 단건 조회 실패, 게시글 없음")
	void read_NotFoundArticle_Fail() throws Exception {
		//given
		given(articleService.read(anyLong())).willThrow(IllegalArgumentException.class);
		//when
		//then
		mockMvc.perform(
				get("/api/articles/{id}", 1L)
		).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시글: 삭제 실패, 게시글 없음")
	void delete_NotFoundArticle_Fail() throws Exception {
		//given
		doThrow(IllegalArgumentException.class).when(articleService).delete(anyLong());
		//when
		//then
		mockMvc.perform(
				delete("/api/articles/{id}", 1L)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("게시글: 수정, 실패, 게시글 없음")
	void update_NotFoundArticle_Fail() throws Exception {
		//given
		given(articleService.update(anyLong(), any())).willThrow(IllegalArgumentException.class);
		//when
		//then
		mockMvc.perform(
				patch("/api/articles/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new ArticleUpdateRequest(
								"수정제목",
								"수정내용",
								"수정썸네일"
						)))
				)
				.andExpect(status().isBadRequest());
	}

}
