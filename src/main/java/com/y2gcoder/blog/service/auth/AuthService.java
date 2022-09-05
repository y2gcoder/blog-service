package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(SignUpRequest req) {
		validateSignUpInfo(req);
		memberRepository.save(new Member(req.getEmail(), passwordEncoder.encode(req.getPassword()), MemberRole.ROLE_USER));
	}

	private void validateSignUpInfo(SignUpRequest req) {
		if(memberRepository.existsByEmail(req.getEmail())) {
			throw new IllegalArgumentException(req.getEmail()+"은 이미 회원가입한 이메일입니다.");
		}
	}

}
