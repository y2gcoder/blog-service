package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.config.token.TokenHelper;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	AuthService authService;
	@Mock
	MemberRepository memberRepository;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	TokenHelper accessTokenHelper;
	@Mock
	TokenHelper refreshTokenHelper;

	@BeforeEach
	void beforeEach() {
		authService = new AuthService(
				memberRepository,
				passwordEncoder,
				accessTokenHelper,
				refreshTokenHelper
		);
	}


	@Test
	@DisplayName("인증: 회원가입 성공")
	void signUp_Normal_Success() {
		//given
		SignUpRequest req = createSignUpRequest();

		//when
		authService.signUp(req);

		//then
		verify(passwordEncoder).encode(req.getPassword());
		verify(memberRepository).save(any());
	}

	@Test
	@DisplayName("인증: 회원가입 실패, 중복 이메일")
	void signUp_DuplicateEmail_Fail() {
		//given
		given(memberRepository.existsByEmail(anyString())).willReturn(true);

		//when, then
		assertThatThrownBy(() -> authService.signUp(createSignUpRequest()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private static SignUpRequest createSignUpRequest() {
		return new SignUpRequest("email@email.com", "12345");
	}

	@Test
	@DisplayName("인증: 로그인 성공")
	void signIn_Normal_Success() {
		//given
		given(memberRepository.findByEmail(anyString()))
				.willReturn(Optional.of(createMember()));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(accessTokenHelper.createToken(any())).willReturn("access");
		given(refreshTokenHelper.createToken(any())).willReturn("refresh");

		//when
		SignInResponse res = authService.signIn(createSignInRequest());
		//then
		assertThat(res.getAccessToken()).isEqualTo("access");
		assertThat(res.getRefreshToken()).isEqualTo("refresh");
	}

	@Test
	@DisplayName("인증: 로그인 실패, 없는 사용자 이메일")
	void signIn_NotFoundMemberEmail_Fail() {
		//given
		given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> authService.signIn(createSignInRequest()))
				.isInstanceOf(SignInFailureException.class);
	}

	@Test
	@DisplayName("인증: 로그인 실패, 비밀번호 불일치")
	void signIn_InvalidPassword_Fail() {
		//given
		given(memberRepository.findByEmail(anyString()))
				.willReturn(Optional.of(createMember()));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
		//when
		//then
		assertThatThrownBy(() -> authService.signIn(createSignInRequest()))
				.isInstanceOf(SignInFailureException.class);
	}

	private static Member createMember() {
		return new Member("email@email.com", "12345", MemberRole.ROLE_USER);
	}

	private static SignInRequest createSignInRequest() {
		return new SignInRequest("email@email.com", "12345");
	}

}