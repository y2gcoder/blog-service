package com.y2gcoder.blog.service.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "댓글 수정 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {

	@ApiModelProperty(value = "댓글 내용", notes = "댓글 내용을 입력해주세요.", required = true, example = "comment content")
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String content;

}
