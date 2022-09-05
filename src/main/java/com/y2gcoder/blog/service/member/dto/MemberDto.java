package com.y2gcoder.blog.service.member.dto;

import com.y2gcoder.blog.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private Long id;
	private String email;
	private String role;

	public MemberDto(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.role = member.getRole().toString();
	}
}
