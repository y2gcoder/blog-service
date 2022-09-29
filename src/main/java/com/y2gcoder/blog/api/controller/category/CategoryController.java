package com.y2gcoder.blog.api.controller.category;

import com.y2gcoder.blog.service.category.CategoryService;
import com.y2gcoder.blog.service.category.dto.CategoryCreateRequest;
import com.y2gcoder.blog.api.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.y2gcoder.blog.api.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Category Controller", tags = "Category")
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController {
	private final CategoryService categoryService;

	@ApiOperation(value = "모든 카테고리 조회", notes = "모든 카테고리 조회")
	@GetMapping
	@ResponseStatus(OK)
	public ApiResponse readAll() {
		return success(categoryService.readAll());
	}

	@ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성한다.")
	@PostMapping
	@ResponseStatus(CREATED)
	public ApiResponse create(@Valid @RequestBody CategoryCreateRequest req) {
		categoryService.create(req);
		return success();
	}

	@ApiOperation(value = "카테고리 삭제", notes = "카테고리 삭제")
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public ApiResponse delete(@ApiParam(value = "카테고리 ID", required = true) @PathVariable Long id) {
		categoryService.delete(id);
		return success();
	}
}
