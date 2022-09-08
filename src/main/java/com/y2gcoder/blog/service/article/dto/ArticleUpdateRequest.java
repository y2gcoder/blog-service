package com.y2gcoder.blog.service.article.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "게시글 수정 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleUpdateRequest {
	@ApiModelProperty(value = "제목", notes = "게시글 제목을 입력해주세요.", required = true, example = "my title")
	@NotBlank(message = "게시글 제목을 입력해주세요.")
	private String title;

	@ApiModelProperty(value = "내용", notes = "게시글 내용을 입력해주세요.", required = true, example = "my content")
	@NotBlank(message = "게시글 내용을 입력해주세요.")
	private String content;

	@ApiModelProperty(value = "게시글 썸네일 URL", notes = "게시글 썸네일 URL을 입력해주세요.")
	private String thumbnailUrl;

}
