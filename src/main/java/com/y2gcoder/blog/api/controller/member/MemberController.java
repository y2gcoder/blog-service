package com.y2gcoder.blog.api.controller.member;

import com.y2gcoder.blog.service.member.MemberService;
import com.y2gcoder.blog.api.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(value = "Member Controller", tags = "Member")
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
	private final MemberService memberService;

	@ApiOperation(value = "사용자 정보 조회", notes = "id를 이용해서 사용자 정보를 조회한다. ")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse find(@ApiParam(value = "사용자 ID", required = true) @PathVariable Long id) {
		return ApiResponse.success(memberService.find(id));
	}

	@ApiOperation(value = "사용자 정보 삭제", notes = "사용자 정보를 삭제한다. 관리자와 당사자만 가능")
	@PreAuthorize("@memberGuard.check(#id)")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse delete(@ApiParam(value = "사용자 ID", required = true) @PathVariable Long id) {
		memberService.delete(id);
		return ApiResponse.success();
	}
}
