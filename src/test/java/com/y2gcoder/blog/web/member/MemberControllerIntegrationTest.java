package com.y2gcoder.blog.web.member;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.init.TestInitDB;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.AuthService;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerIntegrationTest {
	@Autowired
	WebApplicationContext context;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestInitDB initDB;

	@Autowired
	AuthService authService;
	@Autowired
	MemberRepository memberRepository;

	@BeforeEach
	void beforeEach() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		initDB.initDB();
	}

	@Test
	@DisplayName("회원: 조회 성공")
	void find_Normal_Success() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);

		//when
		//then
		mockMvc.perform(
				get("/api/members/{id}", member.getId())
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원: 삭제 성공")
	void delete_Normal_Success() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);
		SignInResponse signInRes = authService.signIn(new SignInRequest(initDB.getMemberEmail1(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", member.getId())
						.header("Authorization", signInRes.getAccessToken())
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원: 삭제 성공, 관리자")
	void delete_ByAdmin_Success() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);
		SignInResponse adminSignInRes = authService.signIn(new SignInRequest(initDB.getAdminEmail(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", member.getId())
						.header("Authorization", adminSignInRes.getAccessToken())
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원: 삭제 실패, 토큰이 없을 때")
	void delete_NoToken_Fail() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", member.getId())
				)
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("회원: 삭제 실패, 관리자 x, 자원소유자 x")
	void delete_NotResourceOwner_Fail() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);
		SignInResponse signInRes = authService.signIn(new SignInRequest(initDB.getMemberEmail2(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", member.getId())
						.header("Authorization", signInRes.getAccessToken())
		).andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("회원: 삭제 실패, refreshToken")
	void delete_RefreshToken_Fail() throws Exception {
		//given
		Member member = memberRepository.findByEmail(initDB.getMemberEmail1())
				.orElseThrow(
						() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다. email=" + initDB.getMemberEmail1())
				);
		SignInResponse signInRes = authService.signIn(new SignInRequest(initDB.getMemberEmail1(), initDB.getPassword()));
		//when
		//then
		mockMvc.perform(
				delete("/api/members/{id}", member.getId())
						.header("Authorization", signInRes.getRefreshToken())
		).andExpect(status().isUnauthorized());
	}

}
