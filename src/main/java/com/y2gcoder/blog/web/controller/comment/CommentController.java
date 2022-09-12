package com.y2gcoder.blog.web.controller.comment;

import com.y2gcoder.blog.annotation.AssignMemberId;
import com.y2gcoder.blog.service.comment.CommentService;
import com.y2gcoder.blog.service.comment.dto.CommentCreateRequest;
import com.y2gcoder.blog.web.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.y2gcoder.blog.web.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;

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
}
