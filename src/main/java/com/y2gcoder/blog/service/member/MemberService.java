package com.y2gcoder.blog.service.member;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;

	public MemberDto find(Long id) {
		return new MemberDto(
				memberRepository.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾지 못했습니다. id=" + id))
		);
	}

	@Transactional
	public void delete(Long id) {
		Member member = memberRepository
				.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾지 못했습니다. id=" + id));
		memberRepository.delete(member);
	}
}
