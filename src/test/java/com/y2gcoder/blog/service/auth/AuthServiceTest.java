package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	AuthService authService;

	@Mock
	MemberRepository memberRepository;

	@BeforeEach
	void beforeEach() {
		authService = new AuthService(memberRepository);
	}

	@Test
	void signUpTest() {
		//given
		SignUpRequest req = new SignUpRequest("email@email.com", "12345");

		//when
		authService.signUp(req);

		//then
		verify(memberRepository).save(any());
	}
}