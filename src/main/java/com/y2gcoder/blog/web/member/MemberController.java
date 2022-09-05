package com.y2gcoder.blog.web.member;

import com.y2gcoder.blog.service.member.MemberService;
import com.y2gcoder.blog.web.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse find(@PathVariable Long id) {
		return ApiResponse.success(memberService.find(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse delete(@PathVariable Long id) {
		memberService.delete(id);
		return ApiResponse.success();
	}
}
