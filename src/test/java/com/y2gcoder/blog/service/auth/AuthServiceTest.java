package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@InjectMocks
	AuthService authService;
	@Mock
	MemberRepository memberRepository;
	@Mock
	PasswordEncoder passwordEncoder;

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
}