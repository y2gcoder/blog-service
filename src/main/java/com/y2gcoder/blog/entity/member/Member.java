package com.y2gcoder.blog.entity.member;

import com.y2gcoder.blog.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	public Member(String email, String password, MemberRole role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}

}
