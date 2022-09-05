package com.y2gcoder.blog.init;

import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TestInitDB {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	private String adminEmail = "admin@admin.com";
	private String memberEmail1 = "member1@member.com";
	private String memberEmail2 = "member2@member.com";
	private String password = "!q2w3e4r";

	@Transactional
	public void initDB() {
		initTestAdmin();
		initTestUser();
	}

	private void initTestAdmin() {
		memberRepository.save(
				new Member(adminEmail, passwordEncoder.encode(password), MemberRole.ROLE_ADMIN)
		);
	}

	private void initTestUser() {
		memberRepository.saveAll(
				List.of(
						new Member(memberEmail1, passwordEncoder.encode(password), MemberRole.ROLE_USER),
						new Member(memberEmail2, passwordEncoder.encode(password), MemberRole.ROLE_USER)
				)
		);
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public String getMemberEmail1() {
		return memberEmail1;
	}

	public String getMemberEmail2() {
		return memberEmail2;
	}

	public String getPassword() {
		return password;
	}
}
