package com.y2gcoder.blog.web.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.RefreshTokenResponse;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	@InjectMocks
	AuthController authController;
	@Mock
	AuthService authService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	@DisplayName("인증: 회원가입 성공")
	void signUp_Normal_Success() throws Exception {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "!q2w3e4r");
		//when
		//then
		mockMvc.perform(
				post("/api/auth/sign-up")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isCreated());

		verify(authService).signUp(req);
	}

	@Test
	@DisplayName("인증: 로그인 성공")
	void signIn_Normal_Success() throws Exception {
		//given
		SignInRequest req = new SignInRequest("email@email.com", "!q2w3e4r");
		given(authService.signIn(req)).willReturn(new SignInResponse("access", "refresh"));
		//when
		//then
		mockMvc
				.perform(
						post("/api/auth/sign-in")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				).andExpect(status().isOk())
				.andExpect(jsonPath("$.result.data.accessToken").value("access"))
				.andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

		verify(authService).signIn(req);
	}

	@Test
	@DisplayName("인증: 토큰 리프레시 성공")
	void tokenRefresh_Normal_Success() throws Exception {
		//given
		given(authService.refreshAccessToken("refreshToken"))
				.willReturn(new RefreshTokenResponse("accessToken"));
		//when
		//then
		mockMvc.perform(
						post("/api/auth/token-refresh")
								.header("Authorization", "refreshToken")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.data.accessToken").value("accessToken"));
	}
}