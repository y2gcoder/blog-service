package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.comment.Comment;
import com.y2gcoder.blog.entity.member.Member;
import com.y2gcoder.blog.entity.member.MemberRole;
import com.y2gcoder.blog.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class CommentGuard extends Guard {
	private final CommentRepository commentRepository;
	private final List<MemberRole> memberRoles = List.of(MemberRole.ROLE_ADMIN);
	@Override
	protected List<MemberRole> getMemberRoles() {
		return memberRoles;
	}

	@Override
	protected boolean isResourceOwner(Long id) {
		return commentRepository.findById(id)
				.map(Comment::getMember)
				.map(Member::getId)
				.filter(memberId -> memberId.equals(AuthHelper.extractUserId()))
				.isPresent();
	}
}
