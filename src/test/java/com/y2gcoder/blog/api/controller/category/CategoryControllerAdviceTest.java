package com.y2gcoder.blog.api.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.category.CategoryService;
import com.y2gcoder.blog.service.category.dto.CategoryCreateRequest;
import com.y2gcoder.blog.api.advice.ExceptionAdvice;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerAdviceTest {
	@InjectMocks CategoryController categoryController;
	@Mock
	CategoryService categoryService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	@DisplayName("카테고리: 생성 실패, 카테고리명 중복")
	void create_DuplicateName_Fail() throws Exception {
		//given
		CategoryCreateRequest req = new CategoryCreateRequest("category");
		doThrow(IllegalArgumentException.class).when(categoryService).create(any());
		//when
		//then
		mockMvc.perform(
				post("/api/categories")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("카테고리: 삭제 실패, 해당 카테고리 없음")
	void delete_NotFound_Fail() throws Exception {
		//given
		doThrow(IllegalArgumentException.class).when(categoryService).delete(anyLong());
		//when
		//then
		mockMvc.perform(
				delete("/api/categories/{id}", 1L)
		).andExpect(status().isBadRequest());
	}
}
