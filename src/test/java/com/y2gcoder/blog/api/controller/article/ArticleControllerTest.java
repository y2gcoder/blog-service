package com.y2gcoder.blog.api.controller.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.repository.article.ArticleSearchCondition;
import com.y2gcoder.blog.service.article.ArticleService;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.service.article.dto.ArticleUpdateRequest;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
	@InjectMocks ArticleController articleController;
	@Mock
	ArticleService articleService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
	}

	@Test
	@DisplayName("게시글: 생성 성공")
	void create_Normal_Success() throws Exception {
		//given
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
		).andExpect(status().isCreated());

		verify(articleService).create(req);
	}

	@Test
	@DisplayName("게시글: 단건 조회 성공")
	void read_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		//when
		//then
		mockMvc.perform(
				get("/api/articles/{id}", id)
				)
				.andExpect(status().isOk());
		verify(articleService).read(id);
	}

	@Test
	@DisplayName("게시글: 삭제 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		//when
		//then
		mockMvc.perform(
				delete("/api/articles/{id}", id)
				)
				.andExpect(status().isOk());
		verify(articleService).delete(id);
	}

	@Test
	@DisplayName("게시글: 수정, 성공")
	void update_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		ArticleUpdateRequest req = new ArticleUpdateRequest("수정제목", "수정내용", "수정썸네일");
		//when
		mockMvc.perform(
			patch("/api/articles/{id}", id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isOk());
		//then
		verify(articleService).update(id, req);
	}

	@Test
	@DisplayName("게시글: 목록 조회, 성공")
	void readAll_Normal_Success() throws Exception {
		//given
		ArticleSearchCondition cond = new ArticleSearchCondition(
				10,
				null,
				null,
				null
		);
		//when
		mockMvc.perform(
				get("/api/articles")
						.param("size", String.valueOf(cond.getSize()))
				)
				.andExpect(status().isOk());
		//then
		verify(articleService).readAll(cond);
	}
}