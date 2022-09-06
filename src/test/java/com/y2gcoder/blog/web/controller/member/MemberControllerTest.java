package com.y2gcoder.blog.web.controller.member;

import com.y2gcoder.blog.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {
	@InjectMocks
	MemberController memberController;
	@Mock
	MemberService memberService;

	MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
	}

	@Test
	@DisplayName("회원: 조회 성공")
	void find_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		//when
		//then
		mockMvc.perform(
						get("/api/members/{id}", id)
				)
				.andExpect(status().isOk());
		verify(memberService).find(id);
	}

	@Test
	@DisplayName("회원: 삭제 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Long id = 1L;
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", id)
		).andExpect(status().isOk());
		verify(memberService).delete(id);
	}
}