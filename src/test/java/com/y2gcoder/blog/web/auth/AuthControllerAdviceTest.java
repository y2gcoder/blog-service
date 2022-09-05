package com.y2gcoder.blog.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.SignInFailureException;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerAdviceTest {
	@InjectMocks
	AuthController authController;
	@Mock
	AuthService authService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(new ExceptionAdvice()).build();
	}

	@Test
	@DisplayName("로그인: 실패, SignInFailureException")
	void signIn_SignInFailureException_Fail() throws Exception {
		//given
		SignInRequest req = new SignInRequest("email@email.com", "!q2w3e4r");
		given(authService.signIn(any())).willThrow(SignInFailureException.class);
		//when
		//then
		mockMvc.perform(
				post("/api/auth/sign-in")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("로그인: 유효성 검증 실패")
	void signIn_InvalidMethodArgument_Fail() throws Exception {
		//given
		SignUpRequest req = new SignUpRequest("email", "1234567");
		//when
		//then
		mockMvc.perform(
			post("/api/auth/sign-in")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입: 유효성 검증 실패")
	void signUp_InvalidMethodArgument_Fail() throws Exception {
		//given
		SignUpRequest req = new SignUpRequest("", "");
		//when
		//then
		mockMvc.perform(
				post("/api/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입: 실패, 이미 존재하는 이메일")
	void signUp_AlreadyExistsEmail_Fail() throws Exception {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!q2w3e4r");
		doThrow(IllegalArgumentException.class).when(authService).signUp(any());
		//when
		//then
		mockMvc.perform(
				post("/api/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("토큰 재발급: 실패, 잘못된 토큰")
	void tokenRefresh_InvalidToken_Fail() throws Exception {
		//given
		given(authService.refreshAccessToken(anyString())).willThrow(IllegalArgumentException.class);
		//when
		//then
		mockMvc.perform(
				post("/api/auth/token-refresh")
						.header("Authorization", "refreshToken")
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("토큰 재발급: 실패, 토큰 누락")
	void tokenRefresh_MissingRefreshToken_Fail() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
				post("/api/auth/token-refresh")
		).andExpect(status().isBadRequest());
	}
}
