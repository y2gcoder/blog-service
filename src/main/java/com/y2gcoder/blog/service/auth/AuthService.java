package com.y2gcoder.blog.service.auth;

import com.y2gcoder.blog.config.token.TokenHelper;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.auth.dto.SignInRequest;
import com.y2gcoder.blog.service.auth.dto.SignInResponse;
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
	private final TokenHelper accessTokenHelper;
	private final TokenHelper refreshTokenHelper;

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

	@Transactional(readOnly = true)
	public SignInResponse signIn(SignInRequest req) {
		Member member = memberRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("해당 이메일을 찾을 수가 없습니다."));
		validatePassword(req, member);
		TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);
		return new SignInResponse(
				accessTokenHelper.createToken(privateClaims),
				refreshTokenHelper.createToken(privateClaims)
		);

	}

	private void validatePassword(SignInRequest req, Member member) {
		if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}

	private TokenHelper.PrivateClaims createPrivateClaims(Member member) {
		return new TokenHelper.PrivateClaims(
				String.valueOf(member.getId()),
				member.getRole().toString()
		);
	}
}
