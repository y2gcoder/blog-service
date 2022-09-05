package com.y2gcoder.blog.web.member;

import com.y2gcoder.blog.service.member.MemberService;
import com.y2gcoder.blog.web.advice.ExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerAdviceTest {
	@InjectMocks MemberController memberController;
	@Mock
	MemberService memberService;

	MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	@DisplayName("회원: 조회 실패, 회원 없음")
	void find_NotFoundMember_Fail() throws Exception {
		//given
		given(memberService.find(anyLong())).willThrow(IllegalArgumentException.class);
		//when
		//then
		mockMvc.perform(
				get("/api/members/{id}", 1L)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원: 삭제 실패, 회원 없음")
	void delete_NotFoundMember_Fail() throws Exception {
		//given
		doThrow(IllegalArgumentException.class).when(memberService).delete(anyLong());
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", 1L)
				)
				.andExpect(status().isBadRequest());
	}
}
