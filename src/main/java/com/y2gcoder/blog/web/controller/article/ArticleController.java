package com.y2gcoder.blog.web.controller.article;

import com.y2gcoder.blog.annotation.AssignMemberId;
import com.y2gcoder.blog.service.article.ArticleService;
import com.y2gcoder.blog.service.article.dto.ArticleCreateRequest;
import com.y2gcoder.blog.web.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.y2gcoder.blog.web.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Article Controller", tags = "Article")
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@RestController
public class ArticleController {
	private final ArticleService articleService;

	@ApiOperation(value = "게시글 생성", notes = "게시글 생성")
	@PostMapping
	@ResponseStatus(CREATED)
	@AssignMemberId
	public ApiResponse create(@Valid @RequestBody ArticleCreateRequest req) {
		return success(articleService.create(req));
	}

	@ApiOperation(value = "게시글 단건 조회", notes = "게시글 단건 조회")
	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public ApiResponse read(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long id) {
		return success(articleService.read(id));
	}

	@ApiOperation(value = "게시글 삭제", notes = "게시글 삭제")
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public ApiResponse delete(@ApiParam(value = "게시글 ID", required = true) @PathVariable Long id) {
		articleService.delete(id);
		return success();
	}
}
