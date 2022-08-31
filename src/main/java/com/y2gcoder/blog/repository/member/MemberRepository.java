package com.y2gcoder.blog.repository.member;

import com.y2gcoder.blog.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}

