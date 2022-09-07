package com.y2gcoder.blog.service.article.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "게시글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCreateRequest {
	@ApiModelProperty(value = "제목", notes = "게시글 제목을 입력해주세요.", required = true, example = "my title")
	@NotBlank(message = "게시글 제목을 입력해주세요.")
	private String title;

	@ApiModelProperty(value = "내용", notes = "게시글 내용을 입력해주세요.", required = true, example = "my content")
	@NotBlank(message = "게시글 내용을 입력해주세요.")
	private String content;

	@ApiModelProperty(value = "게시글 썸네일 URL", notes = "게시글 썸네일 URL을 입력해주세요.")
	private String thumbnailUrl;

	@ApiModelProperty(value = "카테고리 ID", notes = "카테고리 아이디를 입력해주세요.", required = true, example = "1")
	@NotNull(message = "카테고리 ID를 입력해주세요.")
	@Positive(message = "올바른 카테고리 ID를 입력해주세요.")
	private Long categoryId;

	@ApiModelProperty(hidden = true)
	@Null
	private Long memberId;
}
