package com.y2gcoder.blog.web.controller.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReadAllRequest {
	@NotNull(message = "페이지 크기를 입력해주세요.")
	@Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
	private Integer size;
	private Long categoryId;
	private Long lastArticleId;
	private String searchText;
}
