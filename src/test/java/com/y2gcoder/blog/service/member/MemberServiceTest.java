package com.y2gcoder.blog.service.member;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.member.MemberRepository;
import com.y2gcoder.blog.service.member.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
	@InjectMocks MemberService memberService;
	@Mock
	MemberRepository memberRepository;

	@Test
	@DisplayName("회원: 조회 성공")
	void find_Normal_Success() {
		//given
		Member member = createMember();
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		//when
		MemberDto result = memberService.find(1L);
		//then
		assertThat(result.getEmail()).isEqualTo(member.getEmail());
	}

	@Test
	@DisplayName("회원: 조회 실패, 해당 회원 없음")
	void find_NotFoundMember_Fail() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> memberService.find(1L)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("회원: 삭제 성공")
	void delete_Normal_Success() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
		//when
		memberService.delete(1L);
		//then
		verify(memberRepository).delete(any());
	}

	@Test
	@DisplayName("회원: 삭제 실패, 해당 회원 없음")
	void delete_NotFoundMember_Fail() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> memberService.delete(1L)).isInstanceOf(IllegalArgumentException.class);
	}

	private static Member createMember() {
		return new Member("email@email.com", "!q2w3e4r", MemberRole.ROLE_USER);
	}
}