package com.y2gcoder.blog.web.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.comment.CommentService;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.service.comment.dto.CommentUpdateRequest;
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
class CommentControllerTest {
	@InjectMocks CommentController commentController;

	@Mock
	CommentService commentService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
	}

	@Test
	@DisplayName("댓글: 생성, 성공")
	void create_Normal_Success() throws Exception {
		//given
		CommentCreateRequest req = new CommentCreateRequest(
				"my comment",
				null,
				1L
		);
		//when
		//then
		mockMvc.perform(
				post("/api/comments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isCreated());

		verify(commentService).create(req);
	}

	@Test
	@DisplayName("댓글: 삭제, 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		//when
		//then
		mockMvc.perform(
				delete("/api/comments/{id}", id)
				)
				.andExpect(status().isOk());
		verify(commentService).delete(id);
	}

	@Test
	@DisplayName("댓글: 수정, 성공")
	void update_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		CommentUpdateRequest req = new CommentUpdateRequest("수정댓글");
		//when
		//then
		mockMvc.perform(
				patch("/api/comments/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isOk());
		verify(commentService).update(id, req);
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
				)
				.andExpect(status().isOk());
	}
}