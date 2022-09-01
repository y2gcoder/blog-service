package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final MemberRepository memberRepository;

	@Transactional
	public void signUp(SignUpRequest req) {
		validateSignUpInfo(req);
		memberRepository.save(req.toEntity());
	}

	private void validateSignUpInfo(SignUpRequest req) {
		if(memberRepository.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException(req.getEmail()+"은 이미 회원가입한 이메일입니다.");
		}
	}


}
