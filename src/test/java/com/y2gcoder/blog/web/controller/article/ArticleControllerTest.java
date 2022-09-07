package com.y2gcoder.blog.web.controller.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.article.ArticleService;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}