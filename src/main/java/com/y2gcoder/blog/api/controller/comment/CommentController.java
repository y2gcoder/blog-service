package com.y2gcoder.blog.api.controller.comment;

import com.y2gcoder.blog.annotation.AssignMemberId;
import com.y2gcoder.blog.repository.comment.CommentCondition;
import com.y2gcoder.blog.service.comment.CommentService;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.service.comment.dto.CommentUpdateRequest;
import com.y2gcoder.blog.api.controller.comment.dto.CommentReadAllRequest;
import com.y2gcoder.blog.api.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.y2gcoder.blog.api.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Comment Controller", tags = "Comment")
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentController {
	private final CommentService commentService;

	@ApiOperation(value = "댓글 생성", notes = "댓글 생성")
	@PostMapping
	@ResponseStatus(CREATED)
	@AssignMemberId
	public ApiResponse create(@Valid @RequestBody CommentCreateRequest req) {
		return success(commentService.create(req));
	}

	@ApiOperation(value = "댓글 삭제", notes = "댓글 삭제한다.")
	@PreAuthorize("@commentGuard.check(#id)")
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public ApiResponse delete(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long id) {
		commentService.delete(id);
		return success();
	}

	@ApiOperation(value = "댓글 수정", notes = "댓글 수정")
	@PreAuthorize("@commentGuard.check(#id)")
	@PatchMapping("/{id}")
	@ResponseStatus(OK)
	public ApiResponse update(
			@ApiParam(value = "댓글 ID", required = true) @PathVariable Long id,
			@Valid @RequestBody CommentUpdateRequest req
			) {
			return success(commentService.update(id, req));
	}

	@ApiOperation(value = "댓글 목록 조회", notes = "조건을 가지고 댓글 목록을 조회한다.")
	@GetMapping
	@ResponseStatus(OK)
	public ApiResponse readAll(
			@Valid CommentReadAllRequest req
			) {
		return success(
				commentService.readAll(new CommentCondition(
						req.getSize(),
						req.getArticleId(),
						req.getLastCommentId()
				))
		);
	}
}
