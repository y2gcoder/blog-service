package com.y2gcoder.blog.web.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.comment.CommentService;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerAdviceTest {
	@InjectMocks CommentController commentController;
	@Mock
	CommentService commentService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(commentController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	@DisplayName("게시글: 생성, 실패, 회원 없음")
	void create_NotFoundMember_Fail() throws Exception {
		//given
		given(commentService.create(any())).willThrow(IllegalArgumentException.class);
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
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("댓글: 생성, 실패, 게시글 없음")
	void create_NotFoundArticle_Fail() throws Exception {
		//given
		given(commentService.create(any())).willThrow(IllegalArgumentException.class);
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
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("댓글: 삭제, 실패, 댓글 없음")
	void delete_NotFoundComment_Fail() throws Exception {
		//given
		doThrow(IllegalArgumentException.class).when(commentService).delete(anyLong());
		//when
		//then
		mockMvc.perform(
				delete("/api/comments/{id}", 1L)
				)
				.andExpect(status().isBadRequest());
	}
}
