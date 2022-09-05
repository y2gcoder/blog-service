package com.y2gcoder.blog.config.security.guard;

import com.y2gcoder.blog.entity.member.MemberRole;

import java.util.List;

public abstract class Guard {
	public final boolean check(Long id) {
		return hasRole(getMemberRoles()) || isResourceOwner(id);
	}

	abstract protected List<MemberRole> getMemberRoles();

	abstract protected boolean isResourceOwner(Long id);

	private boolean hasRole(List<MemberRole> memberRoles) {
		return AuthHelper.extractMemberRoles().containsAll(memberRoles);
	}

}
