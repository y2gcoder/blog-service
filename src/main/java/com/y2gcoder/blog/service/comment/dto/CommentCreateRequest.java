package com.y2gcoder.blog.service.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "댓글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

	@ApiModelProperty(value = "댓글 내용", notes = "댓글 내용을 입력해주세요.", required = true, example = "comment content")
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String content;

	@ApiModelProperty(hidden = true)
	@Null
	private Long memberId;

	@ApiModelProperty(value = "게시글 ID", notes = "게시글 ID를 입력해주세요.", required = true, example = "1")
	@NotNull(message = "게시글 ID를 입력해주세요.")
	@Positive(message = "올바른 게시글 ID를 입력해주세요. (1 이상)")
	private Long articleId;
}
