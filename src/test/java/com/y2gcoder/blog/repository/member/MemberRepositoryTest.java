package com.y2gcoder.blog.repository.member;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("Member: 저장 성공")
	void saveMember_Normal_Success() {
		//given
		Member member = createMember();
		//when
		memberRepository.save(member);
		//then
		assertThat(memberRepository.findById(member.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Member: 저장 실패, 중복 이메일")
	void saveMember_DuplicateEmail_Fail() {
		//given
		memberRepository.save(createMember());
		//when
		//then
		assertThatThrownBy(() -> memberRepository.save(createMember())).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DisplayName("Member: 권한 변경 성공")
	void changeRole_Normal_Success() {
		//given
		Member member = memberRepository.save(createMember());
		em.flush();
		em.clear();
		//when
		Member findMember = memberRepository.findById(member.getId()).get();
		findMember.changeRole(MemberRole.ROLE_ADMIN);
		em.flush();
		em.clear();
		//then
		Member result = memberRepository.findById(member.getId()).get();
		assertThat(result.getRole()).isEqualTo(MemberRole.ROLE_ADMIN);
	}

	private static Member createMember() {
		return new Member("email@email.com", "12345", MemberRole.ROLE_USER);
	}

}