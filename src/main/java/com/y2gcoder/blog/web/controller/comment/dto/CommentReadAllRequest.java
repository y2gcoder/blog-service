package com.y2gcoder.blog.web.controller.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReadAllRequest {
	@NotNull(message = "페이지 크기를 입력해주세요.")
	@Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
	private Integer size;
	private Long lastCommentId;
	private Long articleId;
}
